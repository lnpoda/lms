package com.example.lms.mapper;

import com.example.lms.dto.CustomerDto;
import com.example.lms.dto.LoanDto;
import com.example.lms.dto.RepaymentScheduleDto;
import com.example.lms.entity.Customer;
import com.example.lms.entity.Loan;
import com.example.lms.entity.RepaymentSchedule;

public class LoanMapper {

    public static LoanDto entityToDto(Loan entity, LoanDto dto) {
        dto.setCustomerDto(CustomerMapper.entityToDto(entity.getCustomer(), new CustomerDto()));
        dto.setRepaymentScheduleDto(RepaymentScheduleMapper.entityToDto(entity.getRepaymentSchedule(),
                new RepaymentScheduleDto()));
        dto.setType(entity.getType());
        dto.setPrincipal(entity.getPrincipal());
        dto.setLoanPaymentStatus(entity.getLoanPaymentStatus());
        dto.setDisbursementDate(entity.getDisbursementDate());
        dto.setDisbursementAmount(entity.getDisbursementAmount());
        return dto;
    }

    public static Loan dtoToEntity(LoanDto dto, Loan entity, Customer customer, RepaymentSchedule repaymentSchedule) {
//        Customer customer = CustomerMapper.dtoToEntity(dto.getCustomerDto(), new Customer());
        entity.setCustomer(customer);
//        RepaymentSchedule repaymentSchedule = RepaymentScheduleMapper.dtoToEntity(dto.getRepaymentScheduleDto(),
//                new RepaymentSchedule());
        entity.setRepaymentSchedule(repaymentSchedule);
        entity.setType(dto.getType());
        entity.setLoanPaymentStatus(dto.getLoanPaymentStatus());
        entity.setPrincipal(dto.getPrincipal());
        entity.setDisbursementDate(dto.getDisbursementDate());
        entity.setDisbursementAmount(dto.getDisbursementAmount());
        return entity;
    }
}
