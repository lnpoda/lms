package com.example.lms.controller;

import com.example.lms.dto.LoanApplicationReviewDto;
import com.example.lms.service.LoanApplicationReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/loanReview")
public class LoanApplicationReviewController {

    private final LoanApplicationReviewService loanApplicationReviewService;
    @GetMapping("/pending")
    public ResponseEntity<List<LoanApplicationReviewDto>> getPendingLoanApplications() {
        List<LoanApplicationReviewDto> loanApplications = loanApplicationReviewService.getPendingLoanApplications();
        return new ResponseEntity<>(loanApplications, HttpStatus.OK);
    }

    @GetMapping("/{applicationReferenceCode}/eligibility")
    public ResponseEntity<Boolean> reviewLoanEligibility(@PathVariable String applicatioReferenceCode) {
        Boolean eligibility = loanApplicationReviewService.reviewLoanEligibility(applicatioReferenceCode);
        return new ResponseEntity<>(eligibility, HttpStatus.OK);
    }
}
