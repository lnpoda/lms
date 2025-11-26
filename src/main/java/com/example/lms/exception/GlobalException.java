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
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now(),
                ex.getMessage(),
                ex.getStackTrace());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                                     WebRequest request) {

        return getPopulatedErrorResponseDto(request.getDescription(false),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now(),
                ex.getMessage(),
                ex.getStackTrace());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(ResourceAlreadyExistsException ex,
                                                                                 WebRequest request) {
        return getPopulatedErrorResponseDto(request.getDescription(false),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                ex.getMessage(),
                ex.getStackTrace());
    }

    private ResponseEntity<ErrorResponseDto> getPopulatedErrorResponseDto(String path,
                                                                          HttpStatus httpStatus,
                                                                          LocalDateTime occurredAt,
                                                                          String message,
                                                                          StackTraceElement[] stackTraceElement) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setPath(path);
        errorResponseDto.setCode(httpStatus.value());
        errorResponseDto.setOccurredAt(occurredAt);
        errorResponseDto.setMessage(message);
        errorResponseDto.setStackTrace(stackTraceElement);

        return new ResponseEntity<>(errorResponseDto, httpStatus);
    }
}
