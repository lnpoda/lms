package com.example.lms.mapper;

import com.example.lms.dto.*;
import com.example.lms.entity.Customer;
import com.example.lms.entity.Loan;
import com.example.lms.entity.LoanApplication;
import com.example.lms.entity.RepaymentSchedule;

public class LoanApplicationMapper {
    public LoanApplication dtoToEntity(LoanApplicationRequestDto dto, LoanApplication entity) {
        CustomerDto customerDto = dto.getCustomerDto();
        Customer customer = CustomerMapper.dtoToEntity(customerDto, new Customer());
        entity.setCustomer(customer);
        entity.setAmount(dto.getAmount());
        entity.setPurpose(dto.getPurpose());
        entity.setCustomerAnnualIncome(dto.getCustomerAnnualIncome());
        entity.setTermMonths(dto.getTermMonths());

        return entity;
    }

    public LoanApplicationResponseDto entityToDto(LoanApplication entity, LoanApplicationResponseDto dto) {
        Loan loan = entity.getLoan();
        dto.setLoanDto(LoanMapper.entityToDto(loan, new LoanDto()));
        RepaymentSchedule repaymentSchedule = entity.getRepaymentSchedule();
        dto.setRepaymentScheduleDto(RepaymentScheduleMapper.entityToDto(repaymentSchedule, new RepaymentScheduleDto()));
        dto.setAmount(entity.getAmount());
        dto.setLoanStatus(entity.getLoanStatus());

        return dto;
    }
}
