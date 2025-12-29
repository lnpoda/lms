package com.example.lms.service;

import com.example.lms.constants.LoanApplicationStatus;
import com.example.lms.constants.LoanPaymentStatus;
import com.example.lms.dto.LoanApplicationReviewDto;
import com.example.lms.dto.LoanDto;
import com.example.lms.dto.RepaymentScheduleDto;
import com.example.lms.entity.Customer;
import com.example.lms.entity.Loan;
import com.example.lms.entity.LoanApplication;
import com.example.lms.entity.RepaymentSchedule;
import com.example.lms.events.LoanApplicationReviewedEvent;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.mapper.LoanApplicationReviewMapper;
import com.example.lms.mapper.LoanMapper;
import com.example.lms.mapper.RepaymentScheduleMapper;
import com.example.lms.repository.CustomerRepository;
import com.example.lms.repository.LoanApplicationRepository;
import com.example.lms.repository.LoanRepository;
import com.example.lms.repository.RepaymentScheduleRepository;
import com.example.lms.utils.business.LoanEligibilityRules;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class LoanApplicationReviewService {

    private final LoanApplicationRepository loanApplicationRepository;

    private final LoanRepository loanRepository;

    private final LoanEligibilityRules loanEligibilityRules;

    private final CustomerRepository customerRepository;

    private final RepaymentScheduleRepository repaymentScheduleRepository;

    private final DisbursementService disbursementService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final RepaymentService repaymentService;

    public List<LoanApplicationReviewDto> getPendingLoanApplications() {
        return loanApplicationRepository.findByLoanApplicationStatusIn(
                List.of(LoanApplicationStatus.SUBMITTED, LoanApplicationStatus.UNDER_REVIEW)
        ).stream()
                .map(loanApplication ->
                        LoanApplicationReviewMapper.loanApplicationEntityToReviewDto(loanApplication,
                                new LoanApplicationReviewDto()))
                .toList();
    }

    public Boolean reviewLoanEligibility(String applicationReferenceCode) {
        LoanApplication loanApplication = loanApplicationRepository
                .findByApplicationReferenceCode(applicationReferenceCode)
                .orElseThrow(()->new ResourceNotFoundException("loanApplication","applicationReferenceCode", applicationReferenceCode));
        log.info("reviewing loan eligibility for loan application: {}", applicationReferenceCode);

        LoanApplicationReviewDto loanApplicationReviewDto = LoanApplicationReviewMapper
                .loanApplicationEntityToReviewDto(loanApplication, new LoanApplicationReviewDto());
        return getLoanEligibility(loanApplicationReviewDto);
    }

    public Boolean getLoanEligibility(LoanApplicationReviewDto loanApplicationReviewDto) {

        Customer customer = getCustomerEntityFromReviewDto(loanApplicationReviewDto);

        LoanApplication loanApplication =
                LoanApplicationReviewMapper
                .reviewDtoToLoanApplicationEntity(loanApplicationReviewDto, new LoanApplication(), customer, null);
        log.info("getting loan eligibility for application: {}", loanApplication.getApplicationReferenceCode());
        return getLoanEligibilityFailures(loanApplication).isEmpty();
    }

    public List<String> getLoanEligibilityFailures(LoanApplication loanApplication) {
        List<String> eligibilityFailures = new ArrayList<>();

        BigDecimal annualIncomeThreshold = loanEligibilityRules.getAnnualIncomeThreshold();
        BigDecimal annualIncome = loanApplication.getCustomerAnnualIncome();
        if (annualIncome.compareTo(annualIncomeThreshold) < 0) {
            eligibilityFailures.add("Annual income insufficient.");
        }
        log.info("annual income of customer {}: {}", loanApplication.getCustomer().getEmail(), annualIncome);

        Integer activeLoanApplicationsThreshold = loanEligibilityRules.getActiveLoanApplicationsThreshold();
        Long customerId = loanApplication.getCustomer().getId();
        Integer numActiveLoanApplications = loanApplicationRepository.findByCustomerId(customerId)
                .stream()
                .filter(application-> application.getLoanApplicationStatus() != LoanApplicationStatus.APPROVED)
                .filter(application->application.getLoanApplicationStatus() != LoanApplicationStatus.REJECTED)
                .toList()
                .size();
        if (numActiveLoanApplications > activeLoanApplicationsThreshold) {
            eligibilityFailures.add("Reached maximum number of active loan applications.");
        }
        log.info("active loan applications for customer {}: {}", loanApplication.getCustomer().getEmail(), numActiveLoanApplications);

        Integer activeLoansThreshold = loanEligibilityRules.getActiveLoansThreshold();
        Integer numActiveLoans = loanRepository.findByCustomerId(customerId)
                .stream()
                .filter(loan->loan.getLoanPaymentStatus() != LoanPaymentStatus.PAID)
                .filter(loan -> loan.getLoanPaymentStatus() != LoanPaymentStatus.NOT_AVAILABLE)
                .toList()
                .size();
        if (numActiveLoans > activeLoansThreshold) {
            eligibilityFailures.add("Reached maximum number of active loans.");
        }
        log.info("active loans for application {}: {}",loanApplication.getApplicationReferenceCode(), numActiveLoans);

        return eligibilityFailures;
    }

    public String approveLoanApplication(String applicationReferenceCode) {

        LoanApplication loanApplication = loanApplicationRepository
                .findByApplicationReferenceCode(applicationReferenceCode)
                .orElseThrow(()->new ResourceNotFoundException("loanApplication",
                        "applicationReferenceCode",
                        applicationReferenceCode));

        LoanApplicationReviewDto loanApplicationReviewDto = LoanApplicationReviewMapper
                .loanApplicationEntityToReviewDto(loanApplication, new LoanApplicationReviewDto());

        LoanDto loanDto = new LoanDto();
        loanDto.setCustomerDto(loanApplicationReviewDto.getLoanApplicationRequestDto().getCustomerDto());
        loanDto.setPrincipal(loanApplicationReviewDto.getLoanApplicationRequestDto().getPrincipal());


        RepaymentSchedule repaymentSchedule = new RepaymentSchedule();
        repaymentScheduleRepository.save(repaymentSchedule);

        RepaymentScheduleDto repaymentScheduleDto = RepaymentScheduleMapper.entityToDto(repaymentSchedule,
                new RepaymentScheduleDto());

        loanApplicationReviewDto.setRepaymentScheduleDto(repaymentScheduleDto);

        loanDto.setRepaymentScheduleDto(repaymentScheduleDto);
        loanApplicationReviewDto.getLoanApplicationResponseDto().setLoanDto(loanDto);

        Customer customer = getCustomerEntityFromReviewDto(loanApplicationReviewDto);


        if (loanApplication.getLoan() == null) {
            Loan loan = LoanMapper.dtoToEntity(loanDto, new Loan(), customer, repaymentSchedule);
            loan.setLoanApplication(loanApplication);
            repaymentSchedule.setLoan(loan);
            loanRepository.save(loan);
            loanApplication.setLoan(loan);
        }

        respondToLoanApplication(loanApplicationReviewDto, LoanApplicationStatus.APPROVED);

        LocalDateTime disbursementDate = disbursementService.calculateDisbursementDate(loanApplication);
        loanRepository.findByLoanApplicationApplicationReferenceCode(applicationReferenceCode)
                        .orElseThrow(()->new ResourceNotFoundException("loan",
                                "applicationReferenceCode",
                                applicationReferenceCode))
                .setDisbursementDate(disbursementDate);
        disbursementService.setDisbursementAmountFromApplicationReferenceCode(applicationReferenceCode);
        repaymentService.generateAndSaveRepaymentSchedule(applicationReferenceCode);

        Loan loan = loanRepository.findByLoanApplicationApplicationReferenceCode(applicationReferenceCode)
                .orElseThrow(()->new ResourceNotFoundException("loan",
                        "applicationReferenceCode",
                        applicationReferenceCode));

        loan.getRepaymentSchedule().setRepaymentSchedule(repaymentScheduleRepository
                .findByLoanLoanApplicationApplicationReferenceCode(applicationReferenceCode)
                .orElseThrow(()->new ResourceNotFoundException("repaymentSchedule",
                        "applicationReferenceCode",
                        applicationReferenceCode))
                .getRepaymentSchedule());

//        repaymentSchedule.setLoan(loan);
        log.info("loan application {} being approved for customer {}", loanApplicationReviewDto.getLoanApplicationResponseDto().getApplicationReferenceCode(), customer.getEmail());

        return loanRepository.findByLoanApplicationApplicationReferenceCode(applicationReferenceCode).get()
                .getLoanReferenceCode();
    }

    public void rejectLoanApplication(String applicationReferenceCode) {
        LoanApplication loanApplication = loanApplicationRepository
                .findByApplicationReferenceCode(applicationReferenceCode)
                .orElseThrow(()->new ResourceNotFoundException("loanApplication",
                        "applicationReferenceCode",
                        applicationReferenceCode));

        LoanApplicationReviewDto loanApplicationReviewDto = LoanApplicationReviewMapper
                .loanApplicationEntityToReviewDto(loanApplication, new LoanApplicationReviewDto());


        log.info("loan application {} being rejected for customer {}", loanApplication.getApplicationReferenceCode(), loanApplication.getCustomer().getEmail());
        respondToLoanApplication(loanApplicationReviewDto, LoanApplicationStatus.REJECTED);
    }

    private void respondToLoanApplication(LoanApplicationReviewDto loanApplicationReviewDto,
                                          LoanApplicationStatus resultingLoanApplicationStatus) {

        String applicationReferenceCode = loanApplicationReviewDto
                .getLoanApplicationResponseDto()
                .getApplicationReferenceCode();

        LoanApplication existingLoanApplication = loanApplicationRepository
                .findByApplicationReferenceCode(applicationReferenceCode)
                .orElseThrow(()->new ResourceNotFoundException("loan",
                        "applicationReferenceCode",
                        applicationReferenceCode));

        existingLoanApplication.setLoanApplicationStatus(resultingLoanApplicationStatus);
        loanApplicationRepository.save(existingLoanApplication);

        LoanApplicationReviewedEvent loanApplicationReviewedEvent = new LoanApplicationReviewedEvent(existingLoanApplication);
        applicationEventPublisher.publishEvent(loanApplicationReviewedEvent);

    }

    private Customer getCustomerEntityFromReviewDto(LoanApplicationReviewDto loanApplicationReviewDto) {

        return customerRepository.findByMobileNumber(loanApplicationReviewDto
                        .getLoanApplicationRequestDto()
                        .getCustomerDto()
                        .getMobileNumber())
                .orElseThrow(()->new ResourceNotFoundException("customer", "mobileNumber", loanApplicationReviewDto
                        .getLoanApplicationRequestDto()
                        .getCustomerDto()
                        .getMobileNumber()));
    }

    private BigDecimal calculatePrincipal(LoanApplication loanApplication) {
        if (loanApplication.getCustomerAnnualIncome().multiply(BigDecimal.valueOf(2))
                .compareTo(loanApplication.getPrincipal()) >= 0) {
            return loanApplication.getPrincipal();
        } else {
            return loanApplication
                    .getCustomerAnnualIncome()
                    .multiply(BigDecimal.valueOf(2));
        }
    }
}
