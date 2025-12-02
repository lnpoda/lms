package com.example.lms.mapper;

import com.example.lms.dto.RepaymentScheduleEntryDto;
import com.example.lms.entity.RepaymentScheduleEntry;

public class RepaymentScheduleEntryMapper {

    public static RepaymentScheduleEntryDto entityToDto(RepaymentScheduleEntry entity, RepaymentScheduleEntryDto dto) {
        dto.setPrincipal(entity.getPrincipal());
        dto.setInterest(entity.getInterest());
        dto.setDueDate(entity.getDueDate());
        dto.setLoanPaymentStatus(entity.getLoanPaymentStatus());
        return dto;
    }

    public static RepaymentScheduleEntry dtoToEntity(RepaymentScheduleEntryDto dto, RepaymentScheduleEntry entity) {
        entity.setPrincipal(dto.getPrincipal());
        entity.setInterest(dto.getInterest());
        entity.setDueDate(dto.getDueDate());
        entity.setLoanPaymentStatus(dto.getLoanPaymentStatus());
        return entity;
    }
}
