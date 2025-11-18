package com.example.lms.dto;

import com.example.lms.constants.LoanApplicationStatus;
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

    private LoanApplicationStatus loanApplicationStatus;

    private String reviewedBy;

    private String reviewedAt;

    private LoanDto loanDto;


}
