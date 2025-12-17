package com.example.lms.service;

import com.example.lms.constants.LoanPaymentStatus;
import com.example.lms.entity.Loan;
import com.example.lms.repository.LoanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class ReportService {

    private final LoanRepository loanRepository;

    public Map<String, List<Loan>> generateLoanReport() {
        List<Loan> overdueLoans = loanRepository.findByLoanPaymentStatus(LoanPaymentStatus.OVERDUE);
        List<Loan> activeLoans = loanRepository.findByLoanPaymentStatus(LoanPaymentStatus.PENDING);
        Map<String, List<Loan>> result = new HashMap<>();
        result.put("overdueLoans", overdueLoans);
        result.put("activeLoans", activeLoans);
        return result;
    }

    public String generateLoanReportCSV() {
        Map<String, List<Loan>> report = generateLoanReport();
        StringBuilder stringBuilder = new StringBuilder();
        // Headers
        stringBuilder.append("Category, LoanReferenceCode, Principal, PaymentStatus");

        report.forEach((loanCategory, loans) -> {
            loans.forEach((loan -> stringBuilder.append(generateCSVForLoan(loanCategory, loan))));
        });

        return stringBuilder.toString();
    }

    private String generateCSVForLoan(String loanCategory, Loan loan) {

        return loanCategory + ","
                + loan.getLoanReferenceCode() + ","
                + loan.getPrincipal() + ","
                + loan.getLoanPaymentStatus();
    }

    public String generateLoanReportPDF() {
        Map<String, List<Loan>> report = generateLoanReport();
        return null;
    }

    private void PDFGenerator() {}
}
