package com.example.lms.repository;

import com.example.lms.entity.LoanApplication;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LoanApplicationRepository extends CrudRepository<LoanApplication, Long> {

    Optional<LoanApplication> findByApplicationReferenceCode(String loanApplicationReferenceCode);

}
