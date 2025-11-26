package com.example.lms.mapper;

import com.example.lms.constants.LoanApplicationStatus;
import com.example.lms.dto.CustomerDto;
import com.example.lms.dto.LoanApplicationRequestDto;
import com.example.lms.dto.LoanApplicationResponseDto;
import com.example.lms.dto.LoanDto;
import com.example.lms.entity.Customer;
import com.example.lms.entity.Loan;
import com.example.lms.entity.LoanApplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class LoanApplicationMapper {
    public static LoanApplication dtoToEntity(LoanApplicationRequestDto dto, LoanApplication entity, Customer customer) {
//        CustomerDto customerDto = dto.getCustomerDto();
//        Customer customer = CustomerMapper.dtoToEntity(customerDto, new Customer());
        entity.setCustomer(customer);
        entity.setPrincipal(dto.getPrincipal());
        entity.setPurpose(dto.getPurpose());
        entity.setCustomerAnnualIncome(dto.getCustomerAnnualIncome());
        entity.setTermMonths(dto.getTermMonths());
        entity.setApplicationReferenceCode(generateApplicationReferenceCode());

        return entity;
    }

    public static LoanApplicationResponseDto entityToDto(LoanApplication entity, LoanApplicationResponseDto dto) {
        if (entity.getLoan() != null) {
            Loan loan = entity.getLoan();
            dto.setLoanDto(LoanMapper.entityToDto(loan, new LoanDto()));
        }
        dto.setPrincipal(entity.getPrincipal());
        dto.setLoanApplicationStatus(entity.getLoanApplicationStatus());
        dto.setApplicationReferenceCode(entity.getApplicationReferenceCode());

        return dto;
    }

    private static String generateApplicationReferenceCode() {
        return "APP-" + LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-"+ UUID.randomUUID().toString().substring(0,8);
    }
}
