package com.example.lms.dto;

import com.example.lms.constants.LoanApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LoanApplicationResponseDto {

    private BigDecimal principal;

    private LoanApplicationStatus loanApplicationStatus;

    private String reviewedBy;

    private LocalDateTime reviewedAt;

    private String applicationReferenceCode;

    private LoanDto loanDto;

    private RepaymentScheduleDto repaymentScheduleDto;

}
