package com.example.lms.repository;

import com.example.lms.entity.Loan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, Long> {

    public List<Loan> findByCustomerId(Long customerId);
}
