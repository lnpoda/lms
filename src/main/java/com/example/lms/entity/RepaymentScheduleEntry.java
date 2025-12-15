package com.example.lms.entity;


import com.example.lms.constants.LoanPaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter @Setter
@Embeddable
public class RepaymentScheduleEntry {

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    private BigDecimal principal;

    private BigDecimal interest;

    private BigDecimal interestPaymentAmount;

    private BigDecimal principalPaymentAmount;

    private BigDecimal totalPaymentAmount;

    private LoanPaymentStatus loanPaymentStatus;
}
