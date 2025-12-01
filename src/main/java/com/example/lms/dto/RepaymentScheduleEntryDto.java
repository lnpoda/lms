package com.example.lms.dto;

import com.example.lms.constants.LoanPaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class RepaymentScheduleEntryDto {

    private LocalDate dueDate;

    private BigDecimal principal;

    private Integer interest;

    private LoanPaymentStatus loanPaymentStatus;
}
