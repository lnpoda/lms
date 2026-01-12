package com.example.lms.controller;


import com.example.lms.constants.LoanApplicationStatus;
import com.example.lms.dto.LoanApplicationRequestDto;
import com.example.lms.dto.LoanApplicationResponseDto;
import com.example.lms.entity.Customer;
import com.example.lms.entity.LMSUser;
import com.example.lms.entity.LoanApplication;
import com.example.lms.entity.Role;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.CustomerRepository;
import com.example.lms.repository.LMSUserRepository;
import com.example.lms.repository.LoanApplicationRepository;
import com.example.lms.repository.RoleRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application-test.yaml")
public class LoanApplicationControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private TestRestTemplate adminRestTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LMSUserRepository lmsUserRepository;

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
        c.setEmail("bijan85426@icousd.com");
        c.setMobileNumber("5551111");
        customerRepository.save(c);

        adminRestTemplate = testRestTemplate.withBasicAuth("loanadmin@email.com", "54321");
    }

    @Test
    public void testLoanApplicationSubmit_withValidLoanApplicationRequestDto_gives201() throws JSONException {
        JSONObject loanApplicationJson = getLoanApplicationRequestJson();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
//        headers.setBasicAuth("loanadmin@email.com", "54321");

        HttpEntity<String> request = new HttpEntity<>(loanApplicationJson.toString(), headers);

        ResponseEntity<LoanApplicationResponseDto> response = adminRestTemplate.postForEntity("/loanApplication/submit", request, LoanApplicationResponseDto.class);

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        Assertions.assertNotNull(loanApplicationRepository.findByLoanApplicationStatus(LoanApplicationStatus.SUBMITTED));
    }

    @Test
    public void testGetLoanApplicationStatus_withValidApplicationReferenceCode_gives200() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setApplicationReferenceCode("APP-123456");
        loanApplication.setPrincipal(BigDecimal.valueOf(5000));
        loanApplication.setTermMonths(12);
        loanApplication.setPurpose("Car Loan");
        loanApplication.setCustomerAnnualIncome(BigDecimal.valueOf(45000));
        loanApplication.setLoanApplicationStatus(LoanApplicationStatus.SUBMITTED);
        loanApplication.setCreatedAt(LocalDateTime.now());
        loanApplication.setUpdatedAt(LocalDateTime.now());

        Customer customer = customerRepository.findByMobileNumber("5551111")
                .orElseThrow(()->new ResourceNotFoundException("customer", "mobileNumber", "5551111"));

        loanApplication.setCustomer(customer);

        loanApplicationRepository.save(loanApplication);

        ResponseEntity<String> response = adminRestTemplate.getForEntity("/loanApplication/{applicationReferenceCode}/status",
                String.class,
                Map.of("applicationReferenceCode", loanApplication.getApplicationReferenceCode()));

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertNotNull(loanApplicationRepository.findByApplicationReferenceCode(loanApplication.getApplicationReferenceCode()));
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(LoanApplicationStatus.SUBMITTED.toString(), response.getBody().replace("\"", ""));

    }

    @Test
    public void testGetLoanApplicationsFromStatus_withValidApplicationStatus_gives200() {

        Customer customer = customerRepository.findByMobileNumber("5551111")
                .orElseGet(() -> {
                    Customer c = new Customer();
                    c.setName("Alice Johnson");
                    c.setEmail("bijan85426@icousd.com");
                    c.setMobileNumber("5551111");
                    return customerRepository.save(c);
                });

        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setApplicationReferenceCode("APP-123456");
        loanApplication.setPrincipal(BigDecimal.valueOf(5000));
        loanApplication.setTermMonths(12);
        loanApplication.setPurpose("Car Loan");
        loanApplication.setCustomerAnnualIncome(BigDecimal.valueOf(45000));
        loanApplication.setLoanApplicationStatus(LoanApplicationStatus.SUBMITTED);
        loanApplication.setCreatedAt(LocalDateTime.now());
        loanApplication.setUpdatedAt(LocalDateTime.now());
        loanApplication.setCustomer(customer);

        loanApplicationRepository.save(loanApplication);

        ResponseEntity<LoanApplicationResponseDto[]> response = adminRestTemplate.getForEntity("/loanApplication/status?loanApplicationStatus=SUBMITTED",
                LoanApplicationResponseDto[].class);

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().length);
    }

    private JSONObject getLoanApplicationRequestJson() throws JSONException {
        String jsonString = """
                {
                  "principal": 5000,
                  "termMonths": 12,
                  "purpose": "Car Loan",
                  "customerAnnualIncome": 45000,
                  "customerDto": {
                    "name": "Alice Johnson",
                    "email": "bijan85426@icousd.com",
                    "mobileNumber": "5551111"
                  }
                }""";

        return new JSONObject(jsonString);
    }
}
