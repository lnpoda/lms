package com.example.lms.entity;

import com.example.lms.constants.LoanStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
public class RepaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long loanId;

    @Column
    private Date dueDate;

    @Column
    private Long principal;

    @Column
    private LoanStatus status;
}
