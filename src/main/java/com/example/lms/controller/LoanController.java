package com.example.lms.controller;

import com.example.lms.dto.LoanRepaymentDto;
import com.example.lms.service.RepaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("loan")
public class LoanController {

    private final RepaymentService repaymentService;

    @PostMapping("/repayment")
    public ResponseEntity<String> performRepayment(@RequestBody LoanRepaymentDto loanRepaymentDto) {
        String loanReferenceCode = repaymentService.performLoanRepayment(loanRepaymentDto);
        return new ResponseEntity<>(loanReferenceCode, HttpStatus.OK);
    }
}
