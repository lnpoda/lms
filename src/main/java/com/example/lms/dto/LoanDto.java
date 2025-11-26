package com.example.lms.dto;

import com.example.lms.constants.LoanPaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LoanDto {

    private CustomerDto customerDto;

    private BigDecimal principal;

    private String type;

    private LoanPaymentStatus loanPaymentStatus;

    private RepaymentScheduleDto repaymentScheduleDto;

    private LocalDate disbursementDate;

    private BigDecimal disbursementAmount;

}
