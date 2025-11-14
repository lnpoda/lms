package com.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ResponseDto {

    HttpStatus code;

    String path;

}
