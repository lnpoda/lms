package com.example.lms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @GetMapping("/operational")
    public ResponseEntity<HttpStatus> getReport() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
