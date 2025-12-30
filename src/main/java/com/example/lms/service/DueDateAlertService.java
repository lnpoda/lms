package com.example.lms.service;

import com.example.lms.entity.Loan;
import com.example.lms.entity.RepaymentSchedule;
import com.example.lms.entity.RepaymentScheduleEntry;
import com.example.lms.repository.LoanRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class DueDateAlertService {

    private final LoanRepository loanRepository;

    private final NotificationService notificationService;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void scanForLoansAndAlert() {
        loanRepository.findAll()
                .forEach(this::alertForLoan);
    }

    public void alertForLoan(Loan loan) {
        List<RepaymentScheduleEntry> upcomingDues = checkUpcomingDueDatesForLoan(loan);
        if (!upcomingDues.isEmpty()) {
            upcomingDues.forEach(entry-> {
                notificationService
                        .notifyCustomer(loan.getCustomer(), "Upcoming Payment Due - " + entry.getDueDate(), "You have an upcoming payment due on " + entry.getDueDate()
                                + " for the loan with the reference code " + loan.getLoanReferenceCode());

                log.info("implemented alertForLoan... sent due date alert to {}", loan.getCustomer().getEmail());
            });
        }

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
                .filter(entry-> LocalDateTime.now().isAfter(entry.getDueDate().minusDays(10)))
                .toList();
    }
}
