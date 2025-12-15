package com.example.lms.mapper;

import com.example.lms.dto.RepaymentScheduleDto;
import com.example.lms.dto.RepaymentScheduleEntryDto;
import com.example.lms.entity.RepaymentSchedule;
import com.example.lms.entity.RepaymentScheduleEntry;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RepaymentScheduleMapper {

    public static RepaymentSchedule dtoToEntity(RepaymentScheduleDto dto, RepaymentSchedule entity) {

        Map<LocalDateTime, RepaymentScheduleEntry> entries = dto.getRepaymentSchedule().stream()
                        .collect(Collectors.toMap(RepaymentScheduleEntryDto::getDueDate,
                                repaymentScheduleEntryDto ->
                                        RepaymentScheduleEntryMapper.dtoToEntity(repaymentScheduleEntryDto,
                                                new RepaymentScheduleEntry())));
        entity.setRepaymentSchedule(entries);
        return entity;
    }

    public static RepaymentScheduleDto entityToDto(RepaymentSchedule entity, RepaymentScheduleDto dto) {

        List<RepaymentScheduleEntryDto> repaymentScheduleEntryDtos = entity.getRepaymentSchedule().entrySet().stream()
                .map(entry-> entry.getValue())
                .map(entry->
                        RepaymentScheduleEntryMapper.entityToDto(entry, new RepaymentScheduleEntryDto()))
                .toList();
        dto.setRepaymentSchedule(repaymentScheduleEntryDtos);
        return dto;
    }
}
