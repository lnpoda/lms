package com.example.lms.service;

import com.example.lms.constants.LoanPaymentStatus;
import com.example.lms.entity.LoanApplication;
import com.example.lms.entity.RepaymentSchedule;
import com.example.lms.entity.RepaymentScheduleEntry;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.LoanApplicationRepository;
import com.example.lms.repository.RepaymentScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class RepaymentService {

    private final LoanApplicationRepository loanApplicationRepository;

    private final RepaymentScheduleRepository repaymentScheduleRepository;


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

    public LoanPaymentStatus getLoanPaymentStatus(LocalDateTime date, LocalDateTime disbursementDate) {
        if (disbursementDate.isAfter(date)) {
            return LoanPaymentStatus.PENDING;
        } else {
            return LoanPaymentStatus.PRE_DISBURSEMENT;
        }
    }
}
