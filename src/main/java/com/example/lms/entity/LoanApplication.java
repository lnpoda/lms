package com.example.lms.entity;

import com.example.lms.constants.LoanApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private BigDecimal amount;

    @Column
    private int termMonths;

    @Column
    private String purpose;

    @Column
    private BigDecimal customerAnnualIncome;

    @Column
    @Enumerated(EnumType.STRING)
    private LoanApplicationStatus loanApplicationStatus;

    @Column
    private String reviewedBy;

    @Column
    private LocalDateTime reviewedAt;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @OneToOne
    private Customer customer;

    @OneToOne
    private Loan loan;


}
