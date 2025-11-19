package com.example.lms.mapper;

import com.example.lms.dto.CustomerDto;
import com.example.lms.dto.LoanApplicationRequestDto;
import com.example.lms.dto.LoanApplicationResponseDto;
import com.example.lms.dto.LoanDto;
import com.example.lms.entity.Customer;
import com.example.lms.entity.Loan;
import com.example.lms.entity.LoanApplication;

public class LoanApplicationMapper {
    public static LoanApplication dtoToEntity(LoanApplicationRequestDto dto, LoanApplication entity) {
        CustomerDto customerDto = dto.getCustomerDto();
        Customer customer = CustomerMapper.dtoToEntity(customerDto, new Customer());
        entity.setCustomer(customer);
        entity.setAmount(dto.getAmount());
        entity.setPurpose(dto.getPurpose());
        entity.setCustomerAnnualIncome(dto.getCustomerAnnualIncome());
        entity.setTermMonths(dto.getTermMonths());

        return entity;
    }

    public static LoanApplicationResponseDto entityToDto(LoanApplication entity, LoanApplicationResponseDto dto) {
        if (entity.getLoan() != null) {
            Loan loan = entity.getLoan();
            dto.setLoanDto(LoanMapper.entityToDto(loan, new LoanDto()));
        }
        dto.setAmount(entity.getAmount());
        dto.setLoanApplicationStatus(entity.getLoanApplicationStatus());

        return dto;
    }
}
