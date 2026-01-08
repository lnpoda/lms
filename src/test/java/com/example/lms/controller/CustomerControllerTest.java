package com.example.lms.controller;

import com.example.lms.dto.CustomerDto;
import com.example.lms.entity.Customer;
import com.example.lms.repository.CustomerRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application-test.yaml")
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

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
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

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode().value());
        Assertions.assertTrue(customerRepository.findByMobileNumber(customerJson.get("mobileNumber").toString()).isEmpty());
    }

    @Test
    public void testListCustomer_gives200() {

        ResponseEntity<CustomerDto[]> response1 = testRestTemplate.withBasicAuth("loanadmin@email.com", "54321")
                .getForEntity("/customer/list", CustomerDto[].class);

        Customer customer1 = new Customer();
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customerRepository.save(customer2);

        ResponseEntity<CustomerDto[]> response2 = testRestTemplate.withBasicAuth("loanadmin@email.com", "54321")
                .getForEntity("/customer/list", CustomerDto[].class);

        Assertions.assertEquals(HttpStatus.OK.value(), response2.getStatusCode().value());
        Assertions.assertNotNull(response2.getBody());
        Assertions.assertNotNull(response1.getBody());
        Assertions.assertEquals(2,  response2.getBody().length - response1.getBody().length);
    }

    @Test
    public void testGetCustomer_withValidMobileNumber_gives200() {


        Customer customer = new Customer();
        customer.setMobileNumber("9999");
        customerRepository.save(customer);
        ResponseEntity<CustomerDto> response = testRestTemplate.withBasicAuth("loanadmin@email.com", "54321")
                .getForEntity("/customer/get?mobileNumber=9999", CustomerDto.class);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("9999", response.getBody().getMobileNumber());
    }

    private JSONObject getCustomerJson() throws JSONException {
        JSONObject customerJson = new JSONObject();
        customerJson.put("name", "testcustomer3");
        customerJson.put("email", "testmail3@email.com");
        customerJson.put("mobileNumber", String.valueOf(System.currentTimeMillis())); // mobileNumber must be unique, so hardcoding doesn't work

        return customerJson;
    }
}
