package com.example.lms.service;

import com.example.lms.constants.LoanApplicationStatus;
import com.example.lms.entity.LoanApplication;
import com.example.lms.repository.LoanApplicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class LoanApplicationReviewService {

    private final LoanApplicationRepository loanApplicationRepository;

    public List<LoanApplication> getPendingLoanApplications() {
        List<LoanApplication> pendingApplications = loanApplicationRepository.findByLoanApplicationStatusIn(
                List.of(LoanApplicationStatus.SUBMITTED, LoanApplicationStatus.UNDER_REVIEW)
        );

        return pendingApplications;
    }
}
