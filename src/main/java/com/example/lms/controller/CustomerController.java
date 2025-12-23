package com.example.lms.controller;

import com.example.lms.dto.CustomerDto;
import com.example.lms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping("/list")
    public ResponseEntity<List<CustomerDto>> getCustomers() {

        return new ResponseEntity<>(customerService.getCustomers(), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<CustomerDto> getCustomer(@RequestParam String mobileNumber) {

        return new ResponseEntity<>(customerService.getCustomer(mobileNumber), HttpStatus.OK);

    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createCustomer(@RequestBody CustomerDto customerDto) {
        customerService.createCustomer(customerDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
