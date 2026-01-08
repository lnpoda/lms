package com.example.lms.controller;


import com.example.lms.constants.LoanApplicationStatus;
import com.example.lms.dto.LoanApplicationRequestDto;
import com.example.lms.dto.LoanApplicationResponseDto;
import com.example.lms.entity.Customer;
import com.example.lms.entity.LMSUser;
import com.example.lms.entity.Role;
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

import java.util.List;

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
