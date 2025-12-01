package com.example.lms.entity;

import com.example.lms.constants.LoanPaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
public class RepaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MapKeyColumn(name = "due_date")
    @ElementCollection
    Map<LocalDate, RepaymentScheduleEntry> repaymentSchedule = new HashMap<>();

    @OneToOne
    private Loan loan;
}
