package com.example.lms.service;

import com.example.lms.constants.LoanApplicationStatus;
import com.example.lms.constants.LoanPaymentStatus;
import com.example.lms.dto.LoanApplicationRequestDto;
import com.example.lms.entity.LoanApplication;
import com.example.lms.mapper.LoanApplicationMapper;
import com.example.lms.repository.LoanApplicationRepository;
import com.example.lms.repository.LoanRepository;
import com.example.lms.utils.business.LoanEligibilityRules;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class LoanApplicationReviewService {

    private final LoanApplicationRepository loanApplicationRepository;

    private final LoanRepository loanRepository;

    private final LoanEligibilityRules loanEligibilityRules;

    public List<LoanApplication> getPendingLoanApplications() {
        List<LoanApplication> pendingApplications = loanApplicationRepository.findByLoanApplicationStatusIn(
                List.of(LoanApplicationStatus.SUBMITTED, LoanApplicationStatus.UNDER_REVIEW)
        );

        return pendingApplications;
    }

    public Boolean reviewLoanEligibility(LoanApplicationRequestDto loanApplicationRequestDto) {
        LoanApplication loanApplication = LoanApplicationMapper.dtoToEntity(loanApplicationRequestDto, new LoanApplication());
        return getLoanEligibilityFailures(loanApplication).isEmpty();
    }

    public List<String> getLoanEligibilityFailures(LoanApplication loanApplication) {
        List<String> eligibilityFailures = new ArrayList<>();

        BigDecimal annualIncomeThreshold = loanEligibilityRules.getAnnualIncomeThreshold();
        BigDecimal annualIncome = loanApplication.getCustomerAnnualIncome();
        if (annualIncome.compareTo(annualIncomeThreshold) < 0) {
            eligibilityFailures.add("Annual income insufficient.");
        }
        Integer activeLoanApplicationsThreshold = loanEligibilityRules.getActiveLoanApplicationsThreshold();
        Long customerId = loanApplication.getCustomer().getId();
        Integer numActiveLoanApplications = loanApplicationRepository.findByCustomerId(customerId)
                .stream()
                .filter(application-> application.getLoanApplicationStatus() != LoanApplicationStatus.APPROVED)
                .filter(application->application.getLoanApplicationStatus() != LoanApplicationStatus.REJECTED)
                .toList()
                .size();
        if (numActiveLoanApplications > activeLoanApplicationsThreshold) {
            eligibilityFailures.add("Reached maximum number of active loan applications.");
        }

        Integer activeLoansThreshold = loanEligibilityRules.getActiveLoansThreshold();
        Integer numActiveLoans = loanRepository.findByCustomerId(customerId)
                .stream()
                .filter(loan->loan.getLoanPaymentStatus() != LoanPaymentStatus.PAID)
                .filter(loan -> loan.getLoanPaymentStatus() != LoanPaymentStatus.NOT_AVAILABLE)
                .toList()
                .size();
        if (numActiveLoans > activeLoansThreshold) {
            eligibilityFailures.add("Reached maximum number of active loans.");
        }

        return eligibilityFailures;
    }
}
