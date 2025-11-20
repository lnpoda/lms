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

    private BigDecimal amount;

    private LoanApplicationStatus loanApplicationStatus;

    private String reviewedBy;

    private LocalDateTime reviewedAt;

    private LoanDto loanDto;

    private String applicationReferenceCode;


}
