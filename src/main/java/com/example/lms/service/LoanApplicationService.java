package com.example.lms.service;

import com.example.lms.constants.LoanApplicationStatus;
import com.example.lms.dto.LoanApplicationRequestDto;
import com.example.lms.dto.LoanApplicationResponseDto;
import com.example.lms.entity.Customer;
import com.example.lms.entity.LoanApplication;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.mapper.LoanApplicationMapper;
import com.example.lms.repository.CustomerRepository;
import com.example.lms.repository.LoanApplicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final CustomerRepository customerRepository;

    public LoanApplicationResponseDto submitLoanApplication(LoanApplicationRequestDto loanApplicationRequestDto) {
        Customer customer = customerRepository.findByMobileNumber(loanApplicationRequestDto.getCustomerDto().getMobileNumber())
                .orElseThrow(()->new ResourceNotFoundException("customer", "mobileNumber", loanApplicationRequestDto.getCustomerDto().getMobileNumber()));

        LoanApplication loanApplication = LoanApplicationMapper.dtoToEntity(loanApplicationRequestDto, new LoanApplication());
        loanApplication.setCustomer(customer);
        loanApplication.setLoanApplicationStatus(LoanApplicationStatus.SUBMITTED);
        LoanApplication savedLoanApplication = loanApplicationRepository.save(loanApplication);
        return LoanApplicationMapper.entityToDto(savedLoanApplication, new LoanApplicationResponseDto());
    }

    public LoanApplicationStatus getLoanApplicationStatusFromApplicationReferenceCode(String loanApplicationReferenceCode) {
        LoanApplicationResponseDto loanApplicationResponseDto = getLoanApplicationFromApplicationReferenceCode(loanApplicationReferenceCode);
        LoanApplicationStatus loanApplicationStatus = loanApplicationResponseDto.getLoanApplicationStatus();
        if (loanApplicationStatus == null) {
            return LoanApplicationStatus.NOT_AVAILABLE;
        }
        return loanApplicationStatus;
    }

    private LoanApplicationResponseDto getLoanApplicationFromApplicationReferenceCode(String loanApplicationReferenceCode) {
        LoanApplication loanApplication = loanApplicationRepository.findByApplicationReferenceCode(loanApplicationReferenceCode)
                .orElseThrow(()->new ResourceNotFoundException("loan","loanApplicationReferenceCode",loanApplicationReferenceCode));
        return LoanApplicationMapper.entityToDto(loanApplication, new LoanApplicationResponseDto());
    }
}
