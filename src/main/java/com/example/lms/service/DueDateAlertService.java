package com.example.lms.service;

import com.example.lms.entity.Loan;
import com.example.lms.entity.RepaymentSchedule;
import com.example.lms.entity.RepaymentScheduleEntry;
import com.example.lms.repository.LoanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Component
public class DueDateAlertService {

    private final RepaymentService repaymentService;

    private final LoanRepository loanRepository;

    public void alert() {}

    public List<RepaymentScheduleEntry> checkUpcomingDueDatesForLoan(Loan loan) {
        RepaymentSchedule repaymentSchedule = loan.getRepaymentSchedule();
        return scanRepaymentScheduleForUpcomingDueDate(repaymentSchedule);

    }

    private List<RepaymentScheduleEntry> scanRepaymentScheduleForUpcomingDueDate(RepaymentSchedule repaymentSchedule) {
        return repaymentSchedule.getRepaymentSchedule().values().stream()
                .filter(entry->LocalDate.now().isAfter(entry.getDueDate().minusDays(10)))
                .toList();
    }
}
