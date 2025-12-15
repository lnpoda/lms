package com.example.lms.dto;

import com.example.lms.constants.LoanPaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
public class RepaymentScheduleEntryDto {

    private LocalDateTime dueDate;

    private BigDecimal principal;

    private BigDecimal interest;

    private BigDecimal principalPaymentAmount;

    private BigDecimal interestPaymentAmount;

    private BigDecimal totalPaymentAmount;

    private LoanPaymentStatus loanPaymentStatus;
}
