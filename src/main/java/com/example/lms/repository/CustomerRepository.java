package com.example.lms.repository;

import com.example.lms.entity.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    public Optional<Customer> findByMobileNumber(String mobileNumber);
}
