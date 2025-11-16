package com.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LoanApplicationRequestDto {
    private Long id;

    private BigDecimal amount;

    private int termMonths;

    private String purpose;

    private BigDecimal customerAnnualIncome;

    private CustomerDto customerDto;
}
