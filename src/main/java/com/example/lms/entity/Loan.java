package com.example.lms.entity;

import com.example.lms.constants.LoanPaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Column
    private LocalDate disbursementDate;

    @Column
    private BigDecimal disbursementAmount;

    @ManyToOne
    private Customer customer;

    @OneToOne
    private LoanApplication loanApplication;

    @OneToOne
    private RepaymentSchedule repaymentSchedule;
}
