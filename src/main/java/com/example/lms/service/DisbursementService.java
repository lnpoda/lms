package com.example.lms.service;

import com.example.lms.entity.LoanApplication;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.LoanApplicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.lang.Math.max;
import static java.lang.Math.min;

@AllArgsConstructor
@Service
public class DisbursementService {

    private final LoanApplicationRepository loanApplicationRepository;

    public BigDecimal calculateDisbursement(String applicationReferenceCode) {
        LoanApplication loanApplication = loanApplicationRepository
                .findByApplicationReferenceCode(applicationReferenceCode)
                .orElseThrow(()->new ResourceNotFoundException("loanApplication",
                        "applicationReferenceCode",
                        applicationReferenceCode));


        // If applied principle is less than half the annual income, return the applied principle, else return half the annual income
        if (loanApplication.getCustomerAnnualIncome().divide(BigDecimal.valueOf(2)).compareTo(loanApplication.getPrincipal()) >= 0) {
            return loanApplication.getPrincipal();
        } else {
            return loanApplication
                    .getCustomerAnnualIncome()
                    .divide(BigDecimal.valueOf(2));
        }
    }
}
