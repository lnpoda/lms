package com.example.lms.entity;

import com.example.lms.constants.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long customerId;

    @Column
    private Long amount;

    @Column
    private String type;

    @Column
    private PaymentStatus paymentStatus;

    @OneToOne
    private LoanApplication loanApplication;
}
