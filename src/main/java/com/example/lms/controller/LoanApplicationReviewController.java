package com.example.lms.controller;

import com.example.lms.entity.LoanApplication;
import com.example.lms.service.LoanApplicationReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/loanReview")
public class LoanApplicationReviewController {

    private final LoanApplicationReviewService loanApplicationReviewService;
    @GetMapping("/pending")
    public ResponseEntity<List<LoanApplication>> getPendingLoanApplications() {
        List<LoanApplication> loanApplications = loanApplicationReviewService.getPendingLoanApplications();
        return new ResponseEntity<>(loanApplications, HttpStatus.OK);
    }
}
