package com.example.lms.mapper;

import com.example.lms.dto.RepaymentScheduleDto;
import com.example.lms.entity.RepaymentSchedule;

import java.time.LocalDate;

public class RepaymentScheduleMapper {

    public static RepaymentSchedule dtoToEntity(RepaymentScheduleDto dto, RepaymentSchedule entity) {
        entity.setDueDate(LocalDate.now().plusDays(30));
        entity.setLoanPaymentStatus(dto.getLoanPaymentStatus());
        entity.setPrincipal(dto.getPrincipal());
        return entity;
    }

    public static RepaymentScheduleDto entityToDto(RepaymentSchedule entity, RepaymentScheduleDto dto) {
        dto.setDueDate(entity.getDueDate());
        dto.setPrincipal(entity.getPrincipal());
        dto.setLoanPaymentStatus(entity.getLoanPaymentStatus());
        return dto;
    }
}
