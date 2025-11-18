package com.example.lms.entity;

import com.example.lms.constants.LoanPaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private BigDecimal amount;

    @Column
    private String type;

    @Column
    private LoanPaymentStatus loanPaymentStatus;

    @OneToOne
    private Customer customer;

    @OneToOne
    private LoanApplication loanApplication;

    @OneToOne
    private RepaymentSchedule repaymentSchedule;
}
