package com.example.lms.mapper;

import com.example.lms.dto.RepaymentScheduleDto;
import com.example.lms.dto.RepaymentScheduleEntryDto;
import com.example.lms.entity.RepaymentSchedule;
import com.example.lms.entity.RepaymentScheduleEntry;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

public class RepaymentScheduleMapper {

    public static RepaymentSchedule dtoToEntity(RepaymentScheduleDto dto, RepaymentSchedule entity) {

        Map<LocalDate, RepaymentScheduleEntry> entries = dto.getRepaymentSchedule().stream()
                        .collect(Collectors.toMap(RepaymentScheduleEntryDto::getDueDate,
                                repaymentScheduleEntryDto ->
                                        RepaymentScheduleEntryMapper.dtoToEntity(repaymentScheduleEntryDto,
                                                new RepaymentScheduleEntry())));
        entity.setRepaymentSchedule(entries);
        return entity;
    }

    public static RepaymentScheduleDto entityToDto(RepaymentSchedule entity, RepaymentScheduleDto dto) {

        return dto;
    }
}
