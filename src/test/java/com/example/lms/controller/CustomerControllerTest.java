package com.example.lms.controller;

import com.example.lms.repository.CustomerRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {


    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testCreateCustomer_withValidDetailsAndAuthorizedCredentials_createsCustomer() throws JSONException {

        JSONObject customerJson = getCustomerJson();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("loanadmin@email.com", "54321");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(customerJson.toString(), headers);

        ResponseEntity<HttpStatus> response = testRestTemplate.postForEntity("/customer/create", request, HttpStatus.class);

        Assertions.assertEquals(response.getStatusCode().value(), HttpStatus.CREATED.value());
        Assertions.assertNotNull(customerRepository.findByMobileNumber(customerJson.get("mobileNumber").toString()));
    }

    @Test
    public void testCreateCustomer_withValidDetailsAndUnauthorizedCredentials_Gives403() throws JSONException {
        JSONObject customerJson = getCustomerJson();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("loanuser@email.com","12345");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(customerJson.toString(), headers);
        ResponseEntity<HttpStatus> response = testRestTemplate.postForEntity("/customer/create", request, HttpStatus.class);

        Assertions.assertEquals(response.getStatusCode().value(), HttpStatus.FORBIDDEN.value());
        Assertions.assertTrue(customerRepository.findByMobileNumber(customerJson.get("mobileNumber").toString()).isEmpty());
    }

    private JSONObject getCustomerJson() throws JSONException {
        JSONObject customerJson = new JSONObject();
        customerJson.put("name", "testcustomer3");
        customerJson.put("email", "testmail3@email.com");
        customerJson.put("mobileNumber", String.valueOf(System.currentTimeMillis())); // mobileNumber must be unique, so hardcoding doesn't work

        return customerJson;
    }
}
