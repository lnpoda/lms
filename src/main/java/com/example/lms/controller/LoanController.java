package com.example.lms.controller;

import com.example.lms.constants.LoanPaymentStatus;
import com.example.lms.dto.LoanDto;
import com.example.lms.dto.LoanRepaymentDto;
import com.example.lms.service.RepaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/status")
    public ResponseEntity<List<LoanDto>> getLoansWithStatuses(@RequestParam String loanPaymentStatus) {
        List<LoanDto> loans = repaymentService
                .getLoansWithPaymentStatuses(LoanPaymentStatus.valueOf(loanPaymentStatus));
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }
}
