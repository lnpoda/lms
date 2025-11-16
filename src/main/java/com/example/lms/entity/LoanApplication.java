package com.example.lms.entity;

import com.example.lms.constants.LoanStatus;
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

    @OneToOne
    private Customer customer;

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
    private LoanStatus loanStatus;

    @Column
    private String reviewedBy;

    @Column
    private LocalDateTime reviewedAt;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;
}
