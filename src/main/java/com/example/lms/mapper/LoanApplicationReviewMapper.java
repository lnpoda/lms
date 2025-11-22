package com.example.lms.mapper;

import com.example.lms.dto.CustomerDto;
import com.example.lms.dto.LoanApplicationRequestDto;
import com.example.lms.dto.LoanApplicationResponseDto;
import com.example.lms.dto.LoanApplicationReviewDto;
import com.example.lms.entity.Customer;
import com.example.lms.entity.Loan;
import com.example.lms.entity.LoanApplication;

public class LoanApplicationReviewMapper {

    public static LoanApplicationReviewDto loanApplicationEntityToReviewDto(LoanApplication entity, LoanApplicationReviewDto dto){

        LoanApplicationRequestDto loanApplicationRequestDto = loanApplicationEntityToRequestDto(entity, new LoanApplicationRequestDto());
        dto.setLoanApplicationRequestDto(loanApplicationRequestDto);

        LoanApplicationResponseDto loanApplicationResponseDto = LoanApplicationMapper.entityToDto(entity, new LoanApplicationResponseDto());
        dto.setLoanApplicationResponseDto(loanApplicationResponseDto);

        return dto;
    }

    public static LoanApplication reviewDtoToLoanApplicationEntity(LoanApplicationReviewDto dto, LoanApplication entity) {
        Customer customer = CustomerMapper.dtoToEntity(dto.getLoanApplicationRequestDto().getCustomerDto(), new Customer());
        entity.setCustomer(customer);

        Loan loan = LoanMapper.dtoToEntity(dto.getLoanApplicationResponseDto().getLoanDto(), new Loan());
        entity.setLoan(loan);

        entity.setApplicationReferenceCode(dto.getLoanApplicationResponseDto().getApplicationReferenceCode());
        entity.setAmount(dto.getLoanApplicationResponseDto().getAmount());
        entity.setPurpose(dto.getLoanApplicationRequestDto().getPurpose());
        entity.setTermMonths(dto.getLoanApplicationRequestDto().getTermMonths());
        entity.setCustomerAnnualIncome(dto.getLoanApplicationRequestDto().getCustomerAnnualIncome());

        return entity;
    }

    private static LoanApplicationRequestDto loanApplicationEntityToRequestDto(LoanApplication entity, LoanApplicationRequestDto dto) {
        CustomerDto customerDto = CustomerMapper.entityToDto(entity.getCustomer(), new CustomerDto());
        dto.setCustomerDto(customerDto);
        dto.setAmount(entity.getAmount());
        dto.setPurpose(entity.getPurpose());
        dto.setTermMonths(entity.getTermMonths());
        dto.setCustomerAnnualIncome(entity.getCustomerAnnualIncome());

        return dto;
    }
}
