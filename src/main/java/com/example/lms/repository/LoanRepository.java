package com.example.lms.repository;

import com.example.lms.entity.Loan;
import org.springframework.data.repository.CrudRepository;

public interface LoanRepository extends CrudRepository<Loan, Long> {
}
