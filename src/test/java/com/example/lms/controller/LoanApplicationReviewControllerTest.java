package com.example.lms.controller;

import com.example.lms.constants.LoanApplicationStatus;
import com.example.lms.dto.LoanApplicationResponseDto;
import com.example.lms.dto.LoanApplicationReviewDto;
import com.example.lms.entity.Customer;
import com.example.lms.entity.LMSUser;
import com.example.lms.entity.LoanApplication;
import com.example.lms.entity.Role;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.*;
import com.example.lms.service.NotificationService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.yaml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanApplicationReviewControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private TestRestTemplate adminRestTemplate;

    @Autowired
    private LMSUserRepository lmsUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private RepaymentScheduleRepository repaymentScheduleRepository;

    @MockitoBean
    private NotificationService notificationService;

    @BeforeEach
    public void setUp() {
        lmsUserRepository.deleteAll();
        roleRepository.deleteAll();

        LMSUser lmsUser = new LMSUser();
        lmsUser.setEmail("loanuser@email.com");
        lmsUser.setPassword("{noop}12345");


        Role userRole = new Role();
        userRole.setRole("ROLE_APPLICANT");
        roleRepository.save(userRole);

        lmsUser.setRole(userRole);
        lmsUserRepository.save(lmsUser);

        LMSUser admin = new LMSUser();
        admin.setEmail("loanadmin@email.com");
        admin.setPassword("{noop}54321");


        Role adminRole = new Role();
        adminRole.setRole("ROLE_ADMIN");
        roleRepository.save(adminRole);

        admin.setRole(adminRole);
        lmsUserRepository.save(admin);

        Customer c = new Customer();
        c.setName("Alice Johnson");
        c.setEmail("alice@example.com");
        c.setMobileNumber("5551111");
        customerRepository.save(c);

        adminRestTemplate = testRestTemplate.withBasicAuth("loanadmin@email.com", "54321");
    }

    @Test
    public void testGetPendingLoanApplications_gives200() {
        // Create pending loan applications
        Customer customer = customerRepository.findByMobileNumber("5551111")
                .orElseGet(() -> {
                    Customer c = new Customer();
                    c.setName("Alice Johnson");
                    c.setEmail("alice@example.com");
                    c.setMobileNumber("5551111");
                    return customerRepository.save(c);
                });

        LoanApplication pending1 = new LoanApplication();
        pending1.setApplicationReferenceCode("APP-PENDING-001");
        pending1.setPrincipal(BigDecimal.valueOf(3000));
        pending1.setTermMonths(12);
        pending1.setPurpose("Education Loan");
        pending1.setCustomerAnnualIncome(BigDecimal.valueOf(40000));
        pending1.setLoanApplicationStatus(LoanApplicationStatus.SUBMITTED);
        pending1.setCreatedAt(LocalDateTime.now());
        pending1.setUpdatedAt(LocalDateTime.now());
        pending1.setCustomer(customer);
        loanApplicationRepository.save(pending1);

        LoanApplication pending2 = new LoanApplication();
        pending2.setApplicationReferenceCode("APP-PENDING-002");
        pending2.setPrincipal(BigDecimal.valueOf(7000));
        pending2.setTermMonths(24);
        pending2.setPurpose("Home Renovation");
        pending2.setCustomerAnnualIncome(BigDecimal.valueOf(60000));
        pending2.setLoanApplicationStatus(LoanApplicationStatus.SUBMITTED);
        pending2.setCreatedAt(LocalDateTime.now());
        pending2.setUpdatedAt(LocalDateTime.now());
        pending2.setCustomer(customer);
        loanApplicationRepository.save(pending2);



        ResponseEntity<LoanApplicationReviewDto[]> response = adminRestTemplate
                .getForEntity("/loanReview/pending", LoanApplicationReviewDto[].class);

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(2, response.getBody().length);

    }

    @Test
    public void testReviewLoanEligibility_withValidApplicationReferenceCode_gives200() {

        Customer customer = customerRepository.findByMobileNumber("5551111")
                .orElseGet(() -> {
                    Customer c = new Customer();
                    c.setName("Alice Johnson");
                    c.setEmail("alice@example.com");
                    c.setMobileNumber("5551111");
                    return customerRepository.save(c);
                });

        LoanApplication pending1 = new LoanApplication();
        pending1.setApplicationReferenceCode("APP-PENDING-001");
        pending1.setPrincipal(BigDecimal.valueOf(3000));
        pending1.setTermMonths(12);
        pending1.setPurpose("Education Loan");
        pending1.setCustomerAnnualIncome(BigDecimal.valueOf(40000));
        pending1.setLoanApplicationStatus(LoanApplicationStatus.SUBMITTED);
        pending1.setCreatedAt(LocalDateTime.now());
        pending1.setUpdatedAt(LocalDateTime.now());
        pending1.setCustomer(customer);
        loanApplicationRepository.save(pending1);

        ResponseEntity<Boolean> response = adminRestTemplate.getForEntity("/loanReview/{applicationReferenceCode}/eligibility",
                Boolean.class, Map.of("applicationReferenceCode","APP-PENDING-001"));

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody());
    }

    @Test
    public void testApproveLoanApplication_withValidApplicationReferenceCode_gives201() throws JSONException {

        JSONObject json = new JSONObject("""
    {
      "principal": 3000,
      "termMonths": 12,
      "purpose": "Education Loan",
      "customerAnnualIncome": 40000,
      "customerDto": {
        "name": "Alice Johnson",
        "email": "alice@example.com",
        "mobileNumber": "5551111"
      }
    }
    """);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

        // Submit the loan application
        ResponseEntity<LoanApplicationResponseDto> submitResponse =
                adminRestTemplate.postForEntity("/loanApplication/submit", request, LoanApplicationResponseDto.class);

        String applicationReferenceCode = submitResponse.getBody().getApplicationReferenceCode();



        ResponseEntity<String> response = adminRestTemplate.postForEntity("/loanReview/{applicationReferenceCode}/approve",
                null,
                String.class,
                Map.of("applicationReferenceCode",applicationReferenceCode));

        LoanApplication approvedLoanApplication = loanApplicationRepository.findByApplicationReferenceCode(applicationReferenceCode)
                .orElseThrow(()->new ResourceNotFoundException("loanApplication", "applicationReferenceCode", applicationReferenceCode));
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        Assertions.assertEquals(LoanApplicationStatus.APPROVED, approvedLoanApplication.getLoanApplicationStatus());
    }

    @Test
    public void testRejectLoanApplication_withValidApplicationReferenceCode_gives201() throws JSONException {

        JSONObject json = new JSONObject("""
    {
      "principal": 3000,
      "termMonths": 12,
      "purpose": "Education Loan",
      "customerAnnualIncome": 40000,
      "customerDto": {
        "name": "Alice Johnson",
        "email": "alice@example.com",
        "mobileNumber": "5551111"
      }
    }
    """);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

        // Submit the loan application
        ResponseEntity<LoanApplicationResponseDto> submitResponse =
                adminRestTemplate.postForEntity("/loanApplication/submit", request, LoanApplicationResponseDto.class);

        String applicationReferenceCode = submitResponse.getBody().getApplicationReferenceCode();

        ResponseEntity<String> response = adminRestTemplate.postForEntity("/loanReview/{applicationReferenceCode}/reject",
                null,
                String.class,
                Map.of("applicationReferenceCode",applicationReferenceCode));

        LoanApplication approvedLoanApplication = loanApplicationRepository.findByApplicationReferenceCode(applicationReferenceCode)
                .orElseThrow(()->new ResourceNotFoundException("loanApplication", "applicationReferenceCode", applicationReferenceCode));
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
        Assertions.assertEquals(LoanApplicationStatus.REJECTED, approvedLoanApplication.getLoanApplicationStatus());
    }
}
