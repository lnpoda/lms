package com.example.lms.service;

import com.example.lms.entity.Loan;
import com.example.lms.entity.RepaymentSchedule;
import com.example.lms.entity.RepaymentScheduleEntry;
import com.example.lms.repository.LoanRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Component
public class DueDateAlertService {

    private final LoanRepository loanRepository;

    private final NotificationService notificationService;

    @Scheduled(cron = "0 * * * * *")
    public void scanForLoansAndAlert() {
        loanRepository.findAll()
                .forEach(this::alertForLoan);
    }

    public void alertForLoan(Loan loan) {
        List<RepaymentScheduleEntry> upcomingDues = checkUpcomingDueDatesForLoan(loan);
        if (!upcomingDues.isEmpty()) {
            upcomingDues.forEach(entry->notificationService
                            .notifyCustomer("You have an upcoming payment due on "+entry.getDueDate()
                                    +" for the loan with the reference code "+loan.getLoanReferenceCode()));
        }
        System.out.println("implemented alertForLoan................");
    }

    private List<RepaymentScheduleEntry> checkUpcomingDueDatesForLoan(Loan loan) {
        RepaymentSchedule repaymentSchedule = loan.getRepaymentSchedule();
        if (repaymentSchedule == null) {
            return List.of();
        }
        return scanRepaymentScheduleForUpcomingDueDate(repaymentSchedule);

    }

    private List<RepaymentScheduleEntry> scanRepaymentScheduleForUpcomingDueDate(RepaymentSchedule repaymentSchedule) {
        return repaymentSchedule.getRepaymentSchedule().values().stream()
                .filter(entry->LocalDate.now().isAfter(entry.getDueDate().minusDays(10)))
                .toList();
    }
}
