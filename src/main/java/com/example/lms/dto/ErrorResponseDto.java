package com.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ErrorResponseDto {

    int code;

    String path;

    LocalDateTime occurredAt;

    String message;
}
