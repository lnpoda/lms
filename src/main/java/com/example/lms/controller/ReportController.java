package com.example.lms.controller;

import com.example.lms.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/operational")
    public ResponseEntity<HttpStatus> getReport() {
        reportService.generateAndSaveLoanReportCSV("./reports/loan_report.csv");
        reportService.generateAndSaveLoanReportPDF();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
