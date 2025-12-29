package com.example.lms.service;

import com.example.lms.dto.CustomerDto;
import com.example.lms.entity.Customer;
import com.example.lms.exception.ResourceAlreadyExistsException;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.mapper.CustomerMapper;
import com.example.lms.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public Customer createCustomer(CustomerDto customerDto) {

        if (customerRepository.findByMobileNumber(customerDto.getMobileNumber()).isPresent()) {
            throw new ResourceAlreadyExistsException("customer",
                    "mobileNumber",
                    customerDto.getMobileNumber());
        }
        Customer customer = CustomerMapper.dtoToEntity(customerDto, new Customer());
        log.info("creating customer: {}", customer);
        return customerRepository.save(customer);
    }

    public CustomerDto getCustomer(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(()->new ResourceNotFoundException("customer", "mobile", mobileNumber));
        log.info("getting customer: {}", customer);

        return CustomerMapper.entityToDto(customer, new CustomerDto());

    }

    public List<CustomerDto> getCustomers() {
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                .map(customer -> CustomerMapper.entityToDto(customer, new CustomerDto()))
                .toList();
    }
}
