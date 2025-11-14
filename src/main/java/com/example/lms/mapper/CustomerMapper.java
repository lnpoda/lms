package com.example.lms.mapper;

import com.example.lms.dto.CustomerDto;
import com.example.lms.entity.Customer;

public class CustomerMapper {

    public static Customer dtoToEntity(CustomerDto dto, Customer entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setMobileNumber(dto.getMobileNumber());

        return entity;
    }


    public static CustomerDto entityToDto(Customer entity, CustomerDto dto) {
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setMobileNumber(entity.getMobileNumber());

        return dto;
    }
}
