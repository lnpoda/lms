package com.example.lms.controller;


import com.example.lms.constants.LoanApplicationStatus;
import com.example.lms.constants.LoanPaymentStatus;
import com.example.lms.entity.*;
import com.example.lms.repository.*;
import com.example.lms.service.ReportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.yaml")
public class ReportControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private TestRestTemplate adminRestTemplate;

    @Autowired
    private LMSUserRepository lmsUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private RepaymentScheduleRepository repaymentScheduleRepository;

    @BeforeEach
    public void setUp() {
        Customer customer = new Customer();
        customer.setName("Alice");
        customer.setEmail("alice@example.com");
        customer.setMobileNumber("5551111");
        customerRepository.save(customer);

        LoanApplication app1 = new LoanApplication();
        app1.setApplicationReferenceCode("APP-TEST-001");
        app1.setPrincipal(BigDecimal.valueOf(3000));
        app1.setTermMonths(12);
        app1.setPurpose("Education");
        app1.setCustomerAnnualIncome(BigDecimal.valueOf(40000));
        app1.setLoanApplicationStatus(LoanApplicationStatus.APPROVED);
        app1.setCreatedAt(LocalDateTime.now());
        app1.setUpdatedAt(LocalDateTime.now());
        app1.setCustomer(customer);
        loanApplicationRepository.save(app1);


        LoanApplication app2 = new LoanApplication();
        app2.setApplicationReferenceCode("APP-TEST-002");
        app2.setPrincipal(BigDecimal.valueOf(5000));
        app2.setTermMonths(12);
        app2.setPurpose("Education");
        app2.setCustomerAnnualIncome(BigDecimal.valueOf(40000));
        app2.setLoanApplicationStatus(LoanApplicationStatus.APPROVED);
        app2.setCreatedAt(LocalDateTime.now());
        app2.setUpdatedAt(LocalDateTime.now());
        app2.setCustomer(customer);
        loanApplicationRepository.save(app2);

        Loan activeLoan = new Loan();
        activeLoan.setLoanReferenceCode("LOAN-ACTIVE-001");
        activeLoan.setPrincipal(BigDecimal.valueOf(3000));
        activeLoan.setType("Education");
        activeLoan.setLoanPaymentStatus(LoanPaymentStatus.PENDING);
        activeLoan.setCustomer(customer);
        activeLoan.setLoanApplication(app1);
        loanRepository.save(activeLoan);

        Loan overdueLoan = new Loan();
        overdueLoan.setLoanReferenceCode("LOAN-OVERDUE-001");
        overdueLoan.setPrincipal(BigDecimal.valueOf(5000));
        overdueLoan.setType("Education");
        overdueLoan.setLoanPaymentStatus(LoanPaymentStatus.OVERDUE);
        overdueLoan.setCustomer(customer);
        overdueLoan.setLoanApplication(app2);
        loanRepository.save(overdueLoan);

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
    public void testGetReport_returns200() {
        ResponseEntity<String> response = adminRestTemplate.getForEntity("/reports/operational", String.class);

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());

        Assertions.assertTrue(Files.exists(Paths.get("./reports/loan_report.csv")));
        Assertions.assertTrue(Files.exists(Paths.get("./reports/overdueLoans_loan_report.pdf")));
        Assertions.assertTrue(Files.exists(Paths.get("./reports/activeLoans_loan_report.pdf")));
    }
}
