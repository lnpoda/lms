package com.example.lms.exception;

import com.example.lms.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception ex,
                                                            WebRequest request) {

        return getPopulatedErrorResponseDto(request.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                                     WebRequest request) {

        return getPopulatedErrorResponseDto(request.getDescription(false),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(ResourceAlreadyExistsException ex,
                                                                                 WebRequest request) {
        return getPopulatedErrorResponseDto(request.getDescription(false),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                ex.getMessage());
    }

    private ResponseEntity<ErrorResponseDto> getPopulatedErrorResponseDto(String path,
                                                                          int code,
                                                                          LocalDateTime occurredAt,
                                                                          String message) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setPath(path);
        errorResponseDto.setCode(code);
        errorResponseDto.setOccurredAt(occurredAt);
        errorResponseDto.setMessage(message);

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }
}
