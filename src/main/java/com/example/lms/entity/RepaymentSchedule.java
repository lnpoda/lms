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
public class RepaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate dueDate;

    @Column
    private BigDecimal principal;

    @Column
    private LoanPaymentStatus loanPaymentStatus;

    @OneToOne
    private Loan loan;
}
