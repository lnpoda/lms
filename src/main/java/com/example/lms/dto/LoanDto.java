package com.example.lms.dto;

import com.example.lms.constants.LoanPaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LoanDto {

    private CustomerDto customerDto;

    private BigDecimal amount;

    private String type;

    private LoanPaymentStatus loanPaymentStatus;

    private RepaymentScheduleDto repaymentScheduleDto;

}
