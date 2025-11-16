package com.example.lms.dto;

import com.example.lms.constants.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LoanApplicationResponseDto {

    private BigDecimal amount;

    private CustomerDto customerDto;

    private LoanStatus loanStatus;

    private String reviewedBy;

    private String reviewedAt;

    private LoanDto loanDto;

    private RepaymentScheduleDto repaymentScheduleDto;
}
