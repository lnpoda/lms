package com.example.lms.repository;

import com.example.lms.constants.LoanPaymentStatus;
import com.example.lms.entity.Loan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends CrudRepository<Loan, Long> {

    public List<Loan> findByCustomerId(Long customerId);

    public Optional<Loan> findByLoanApplicationApplicationReferenceCode(String applicationReferenceCode);

    public Optional<Loan> findByLoanReferenceCode(String loanReferenceCode);

    public List<Loan> findByLoanPaymentStatus(LoanPaymentStatus loanPaymentStatus);
}
