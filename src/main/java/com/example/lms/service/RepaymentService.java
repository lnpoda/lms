package com.example.lms.service;

import com.example.lms.constants.LoanPaymentStatus;
import com.example.lms.entity.Loan;
import com.example.lms.entity.LoanApplication;
import com.example.lms.entity.RepaymentSchedule;
import com.example.lms.entity.RepaymentScheduleEntry;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.LoanApplicationRepository;
import com.example.lms.repository.LoanRepository;
import com.example.lms.repository.RepaymentScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class RepaymentService {

    private final LoanApplicationRepository loanApplicationRepository;

    private final RepaymentScheduleRepository repaymentScheduleRepository;

    private final LoanRepository loanRepository;


    public void generateAndSaveRepaymentSchedule(String applicationReferenceCode) {

        Optional<RepaymentSchedule> repaymentScheduleOptional = repaymentScheduleRepository
                .findByLoanLoanApplicationApplicationReferenceCode(applicationReferenceCode);

        Map<LocalDate, RepaymentScheduleEntry> repaymentScheduleEntries =
                generateRepaymentScheduleEntries(applicationReferenceCode);

        if (repaymentScheduleOptional.isPresent()) {
            RepaymentSchedule repaymentSchedule = repaymentScheduleOptional.get();
            repaymentSchedule.setRepaymentSchedule(repaymentScheduleEntries);
            repaymentScheduleRepository.save(repaymentSchedule);
        } else {
            RepaymentSchedule repaymentSchedule = new RepaymentSchedule();
            repaymentSchedule.setRepaymentSchedule(repaymentScheduleEntries);
            repaymentScheduleRepository.save(repaymentSchedule);
        }

    }

    public Map<LocalDate, RepaymentScheduleEntry> generateRepaymentScheduleEntries(String applicationReferenceCode) {
        LoanApplication loanApplication = loanApplicationRepository.findByApplicationReferenceCode(applicationReferenceCode)
                .orElseThrow(()->new ResourceNotFoundException("loanApplication",
                        "applicationReferenceCode",
                        applicationReferenceCode));

        LocalDateTime startDate = loanApplication.getLoan().getDisbursementDate();
        LocalDateTime endDate = loanApplication.getLoan().getDisbursementDate()
                .plusMonths(loanApplication.getTermMonths());

         return Stream.iterate(startDate, date->date.plusDays(30))
                .filter(date->!date.isAfter(endDate))
                .collect(Collectors.toMap(LocalDateTime::toLocalDate,
                        date-> {
                    RepaymentScheduleEntry repaymentScheduleEntry = new RepaymentScheduleEntry();
                    repaymentScheduleEntry.setPrincipalPaymentAmount(loanApplication.getPrincipal()
                            .divide(BigDecimal.valueOf(loanApplication.getTermMonths()), RoundingMode.HALF_EVEN));
                    repaymentScheduleEntry.setInterestPaymentAmount(loanApplication.getPrincipal()
                            .multiply(BigDecimal.valueOf(0.05)));
                    repaymentScheduleEntry.setLoanPaymentStatus(getLoanPaymentStatus(date,
                            loanApplication
                                    .getLoan()
                                    .getDisbursementDate()));

                    return repaymentScheduleEntry;
                        }));


    }

    public void performLoanRepayment(String loanReferenceCode, BigDecimal repaymentAmount) {
        Loan loan = loanRepository.findByLoanReferenceCode(loanReferenceCode)
                .orElseThrow(()->new ResourceNotFoundException("loan", "loanReferenceCode", loanReferenceCode));

        updateLastRepaymentScheduleEntry(loan.getRepaymentSchedule(), repaymentAmount);
    }

    public LoanPaymentStatus getLoanPaymentStatus(LocalDateTime date, LocalDateTime disbursementDate) {
        if (disbursementDate.isAfter(date)) {
            return LoanPaymentStatus.PENDING;
        } else {
            return LoanPaymentStatus.PRE_DISBURSEMENT;
        }
    }

    public void updateLastRepaymentScheduleEntry(RepaymentSchedule repaymentSchedule,
                                                              BigDecimal updateAmount) {
        repaymentSchedule.getRepaymentSchedule().values()
                .stream()
                .filter(entry -> entry.getLoanPaymentStatus() == LoanPaymentStatus.PENDING)
                .findFirst()

                .ifPresent(entry-> applyRepaymentToEntry(entry, updateAmount) );
    }

    private void applyRepaymentToEntry(RepaymentScheduleEntry entry, BigDecimal totalUpdateAmount) {
        // deduct the paid amount from the principal due payment and save the remaining as amount paid towards interest.
        BigDecimal netInterestPaymentAmount = totalUpdateAmount.subtract(entry.getPrincipalPaymentAmount());
        // if the remaining amount is > 0, calculate towards interest due payment
        if (netInterestPaymentAmount.compareTo(BigDecimal.ZERO) == 1) {
            // TODO: add the case for when paid amount is greater than interest due, and add towards the next payment.

            // if the remaining amount is more than or equal to the due interest payment, mark the entry as paid.
            if (netInterestPaymentAmount.subtract(entry.getInterestPaymentAmount()).compareTo(BigDecimal.ZERO) >= 0 ) {
                entry.setLoanPaymentStatus(LoanPaymentStatus.PAID);
            } else {
                // if the remaining amount is less than the due interest payment, and ...
                // if current date is past due date, mark the entry as 'overdue', else mark as 'pending'
                if (entry.getDueDate().isBefore(LocalDate.now())) {
                    entry.setLoanPaymentStatus(LoanPaymentStatus.OVERDUE);
                } else {
                    // if the current date is before the due date, mark the entry as 'pending'
                    entry.setLoanPaymentStatus(LoanPaymentStatus.PENDING);
                }
            }
        }
    }
}
