package com.example.lms.controller;

import com.example.lms.dto.LoanApplicationReviewDto;
import com.example.lms.service.LoanApplicationReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Boolean> reviewLoanEligibility(@PathVariable String applicationReferenceCode) {
        Boolean eligibility = loanApplicationReviewService.reviewLoanEligibility(applicationReferenceCode);
        return new ResponseEntity<>(eligibility, HttpStatus.OK);
    }

    @PostMapping("/approve")
    public ResponseEntity<HttpStatus> approveLoanApplication(@RequestBody LoanApplicationReviewDto loanApplicationReviewDto) {
        loanApplicationReviewService.approveLoanApplication(loanApplicationReviewDto);
        return new ResponseEntity<>(HttpStatus.CREATED); //TODO: make ResponseEntity carry ResponseDto
    }

    @PostMapping("/reject")
    public ResponseEntity<HttpStatus> rejectLoanApplication(@RequestBody LoanApplicationReviewDto loanApplicationReviewDto) {
        loanApplicationReviewService.rejectLoanApplication(loanApplicationReviewDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //TODO: make ResponseEntity carry ResponseDto
    }

}
