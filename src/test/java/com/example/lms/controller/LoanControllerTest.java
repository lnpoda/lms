package com.example.lms.controller;


import com.example.lms.constants.LoanApplicationStatus;
import com.example.lms.constants.LoanPaymentStatus;
import com.example.lms.dto.RepaymentScheduleDto;
import com.example.lms.entity.*;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.mapper.RepaymentScheduleMapper;
import com.example.lms.repository.*;
import com.example.lms.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.yaml")
public class LoanControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private TestRestTemplate adminRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private RepaymentScheduleRepository repaymentScheduleRepository;

    @Autowired
    private LMSUserRepository lmsUserRepository;

    @Autowired
    private RoleRepository roleRepository;

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

        adminRestTemplate = testRestTemplate.withBasicAuth("loanadmin@email.com", "54321");


        Customer customer = new Customer();
        customer.setName("Alice Johnson");
        customer.setEmail("alice@example.com");
        customer.setMobileNumber("5551111");
        customerRepository.save(customer);


        LoanApplication app = new LoanApplication();
        app.setApplicationReferenceCode("APP-TEST-001");
        app.setPrincipal(BigDecimal.valueOf(3000));
        app.setTermMonths(12);
        app.setPurpose("Education Loan");
        app.setCustomerAnnualIncome(BigDecimal.valueOf(40000));
        app.setLoanApplicationStatus(LoanApplicationStatus.APPROVED);
        app.setCreatedAt(LocalDateTime.now());
        app.setUpdatedAt(LocalDateTime.now());
        app.setCustomer(customer);
        loanApplicationRepository.save(app);


        RepaymentSchedule schedule = new RepaymentSchedule();
        repaymentScheduleRepository.save(schedule);

        RepaymentScheduleEntry entry = new RepaymentScheduleEntry();
        entry.setDueDate(LocalDateTime.now().plusMonths(1));
        entry.setPrincipal(BigDecimal.valueOf(1000));
        entry.setInterest(BigDecimal.valueOf(50));
        entry.setInterestPaymentAmount(BigDecimal.valueOf(50));
        entry.setPrincipalPaymentAmount(BigDecimal.valueOf(1000));
        entry.setTotalPaymentAmount(BigDecimal.valueOf(1050));
        entry.setLoanPaymentStatus(LoanPaymentStatus.PENDING);

        schedule.getRepaymentSchedule().put(entry.getDueDate(), entry);


        Loan loan = new Loan();
        loan.setLoanReferenceCode("LOAN-TEST-001");
        loan.setPrincipal(BigDecimal.valueOf(3000));
        loan.setType("Education");
        loan.setLoanPaymentStatus(LoanPaymentStatus.PENDING);
        loan.setCustomer(customer);
        loan.setLoanApplication(app);
        loan.setRepaymentSchedule(schedule);

        loanRepository.save(loan);

        schedule.setLoan(loan);
        repaymentScheduleRepository.save(schedule);
    }

    @Autowired
    ApplicationContext ctx;

//    @Test
//    void printBeans() {
//        Arrays.stream(ctx.getBeanDefinitionNames())
//                .filter(b -> b.contains("security"))
//                .forEach(System.out::println);
//    }


//    @Disabled
    @Test
    public void testPerformRepayment_withValidLoanRepaymentDto_gives200() throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        String loanRepaymentDtoJson = """
{
  "loanReferenceCode": "LOAN-TEST-001",
  "repaymentAmount": 5000
}
""";
//        RepaymentSchedule scheduleBefore =
//                repaymentScheduleRepository.findByLoanLoanReferenceCode("LOAN-TEST-001")
//                        .orElseThrow(() -> new ResourceNotFoundException("loan", "loanReferenceCode", "LOAN-TEST-001"));
//
//        BigDecimal originalAmount =
//                scheduleBefore.getRepaymentSchedule()
//                        .values()
//                        .iterator()
//                        .next()
//                        .getTotalPaymentAmount();



        HttpEntity<String> request = new HttpEntity<>(loanRepaymentDtoJson, headers);
        ResponseEntity<String> response = adminRestTemplate.postForEntity("/loan/repayment",
                request,
                String.class);

//        JsonNode root = objectMapper.readTree(response.getBody());

//        RepaymentSchedule schedule =
//                repaymentScheduleRepository.findByLoanLoanReferenceCode("LOAN-TEST-001")
//                        .orElseThrow(()->new ResourceNotFoundException("loan", "loanReferenceCode", "LOAN-TEST-001"));

//        BigDecimal repaidAmount =
//                schedule.getRepaymentSchedule()
//                        .values()
//                        .iterator()
//                        .next()
//                        .getTotalPaymentAmount();
//        BigDecimal repaidAmount = repaidEntry.getTotalPaymentAmount();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
//        Assertions.assertEquals(BigDecimal.valueOf(500), originalAmount.subtract(repaidAmount));

        Loan loan = loanRepository.findByLoanReferenceCode("LOAN-TEST-001")
                .orElseThrow();

        Assertions.assertEquals(LoanPaymentStatus.PAID, loan.getLoanPaymentStatus());
    }

    @Test
    public void testGetRepaymentSchedule_withValidLoanReferenceCode_gives200() throws JsonProcessingException {
        ResponseEntity<String> response = adminRestTemplate.getForEntity("/loan/repayments/{loanReferenceCode}", String.class, Map.of("loanReferenceCode", "LOAN-TEST-001"));

        JsonNode root = objectMapper.readTree(response.getBody());

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(root.has("repaymentSchedule"));
    }

    @Test
    public void testGetLoansWithStatuses_withValidLoanPaymentStatus_gives200() throws JsonProcessingException {
        ResponseEntity<String> response = adminRestTemplate.getForEntity("/loan/status?loanPaymentStatus=PENDING", String.class);

        JsonNode root = objectMapper.readTree(response.getBody());

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, root.size());
        Assertions.assertEquals("LOAN-TEST-001", root.get(0).get("loanReferenceCode").asText());

    }
}
