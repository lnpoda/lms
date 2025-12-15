package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @MapKeyColumn(name = "entry_due_date")
//    @CollectionTable(
//            name = "repayment_schedule_entries",
//            joinColumns = @JoinColumn(name = "repayment_schedule_id")
//    )
    @ElementCollection
    Map<LocalDateTime, RepaymentScheduleEntry> repaymentSchedule = new HashMap<>();

    @OneToOne
    private Loan loan;
}
