package com.example.lms.controller;

import com.example.lms.dto.LoanApplicationRequestDto;
import com.example.lms.dto.LoanApplicationResponseDto;
import com.example.lms.service.LoanApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/loanApplication")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    @PostMapping("/submit")
    public ResponseEntity<LoanApplicationResponseDto> submit(@RequestBody LoanApplicationRequestDto loanApplicationRequestDto) {
        LoanApplicationResponseDto loanApplicationResponseDto = loanApplicationService.submitLoanApplication(loanApplicationRequestDto);
        return new ResponseEntity<>(loanApplicationResponseDto, HttpStatus.CREATED);
    }
}
