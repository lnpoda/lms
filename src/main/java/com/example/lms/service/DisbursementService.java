package com.example.lms.service;

import com.example.lms.constants.LoanApplicationStatus;
import com.example.lms.entity.Loan;
import com.example.lms.entity.LoanApplication;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.LoanApplicationRepository;
import com.example.lms.repository.LoanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class DisbursementService {

    private final LoanApplicationRepository loanApplicationRepository;

    private final LoanRepository loanRepository;

    public void setDisbursementAmountFromApplicationReferenceCode(String applicationReferenceCode) {
        LoanApplication loanApplication = loanApplicationRepository
                .findByApplicationReferenceCode(applicationReferenceCode)
                .orElseThrow(()->new ResourceNotFoundException("loanApplication",
                        "applicationReferenceCode",
                        applicationReferenceCode));

        Loan loan = loanRepository.findByLoanApplicationApplicationReferenceCode(applicationReferenceCode)
                .orElseThrow(()->new ResourceNotFoundException("loan",
                        "applicationReferenceCode",
                        applicationReferenceCode));

        if (loan.getDisbursementAmount() == null) {
            loan.setDisbursementAmount(calculateDisbursement(loanApplication));
        }

        loanRepository.save(loan);
    }

    private BigDecimal calculateDisbursement(LoanApplication loanApplication) {

        return loanApplication.getPrincipal().divide(BigDecimal.valueOf(loanApplication.getTermMonths()),
                2,
                RoundingMode.HALF_EVEN);
    }

    public LocalDateTime calculateDisbursementDate(LoanApplication loanApplication){
        if ((loanApplication.getLoanApplicationStatus() == LoanApplicationStatus.APPROVED)) {
            return loanApplication.getReviewedAt().plusDays(5);
        }
        return null;
    }
}
