package com.example.lms.service;

import com.example.lms.constants.LoanApplicationStatus;
import com.example.lms.constants.LoanPaymentStatus;
import com.example.lms.dto.LoanApplicationReviewDto;
import com.example.lms.dto.LoanDto;
import com.example.lms.entity.LoanApplication;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.mapper.LoanApplicationReviewMapper;
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

    public List<LoanApplicationReviewDto> getPendingLoanApplications() {
        return loanApplicationRepository.findByLoanApplicationStatusIn(
                List.of(LoanApplicationStatus.SUBMITTED, LoanApplicationStatus.UNDER_REVIEW)
        ).stream()
                .map(loanApplication ->
                        LoanApplicationReviewMapper.loanApplicationEntityToReviewDto(loanApplication,
                                new LoanApplicationReviewDto()))
                .toList();
    }

    public Boolean reviewLoanEligibility(String applicationReferenceCode) {
        LoanApplication loanApplication = loanApplicationRepository
                .findByApplicationReferenceCode(applicationReferenceCode)
                .orElseThrow(()->new ResourceNotFoundException("loanApplication","applicationReferenceCode", applicationReferenceCode));

        LoanApplicationReviewDto loanApplicationReviewDto = LoanApplicationReviewMapper
                .loanApplicationEntityToReviewDto(loanApplication, new LoanApplicationReviewDto());
        return getLoanEligibility(loanApplicationReviewDto);
    }

    public Boolean getLoanEligibility(LoanApplicationReviewDto loanApplicationReviewDto) {
        LoanApplication loanApplication =
                LoanApplicationReviewMapper
                .reviewDtoToLoanApplicationEntity(loanApplicationReviewDto, new LoanApplication());
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

    public void approveLoanApplication(LoanApplicationReviewDto loanApplicationReviewDto) {

        LoanDto loanDto = new LoanDto();
        loanDto.setCustomerDto(loanApplicationReviewDto.getLoanApplicationRequestDto().getCustomerDto());
        loanDto.setAmount(loanApplicationReviewDto.getLoanApplicationRequestDto().getAmount());
        loanApplicationReviewDto.getLoanApplicationResponseDto().setLoanDto(loanDto);

        respondToLoanApplication(loanApplicationReviewDto, LoanApplicationStatus.APPROVED);
    }

    public void rejectLoanApplication(LoanApplicationReviewDto loanApplicationReviewDto) {
        respondToLoanApplication(loanApplicationReviewDto, LoanApplicationStatus.REJECTED);
    }

    private void respondToLoanApplication(LoanApplicationReviewDto loanApplicationReviewDto,
                                          LoanApplicationStatus resultingLoanApplicationStatus) {
        loanApplicationReviewDto
                .getLoanApplicationResponseDto()
                .setLoanApplicationStatus(resultingLoanApplicationStatus);
        LoanApplication loanApplication = LoanApplicationReviewMapper
                .reviewDtoToLoanApplicationEntity(loanApplicationReviewDto, new LoanApplication());


        loanApplicationRepository.save(loanApplication);
    }
}
