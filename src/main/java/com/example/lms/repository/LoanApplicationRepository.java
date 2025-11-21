package com.example.lms.repository;

import com.example.lms.constants.LoanApplicationStatus;
import com.example.lms.entity.LoanApplication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LoanApplicationRepository extends CrudRepository<LoanApplication, Long> {

    Optional<LoanApplication> findByApplicationReferenceCode(String loanApplicationReferenceCode);

    List<LoanApplication> findByLoanApplicationStatus(LoanApplicationStatus loanApplicationStatus);

    List<LoanApplication> findByLoanApplicationStatusIn(List<LoanApplicationStatus> loanApplicationStatuses);

}
