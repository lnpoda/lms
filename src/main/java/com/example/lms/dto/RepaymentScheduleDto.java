package com.example.lms.dto;

import com.example.lms.constants.LoanApplicationStatus;
import com.example.lms.constants.LoanPaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class RepaymentScheduleDto {

    private Date dueDate;

    private BigDecimal principal;

    private LoanPaymentStatus loanPaymentStatus;

}
