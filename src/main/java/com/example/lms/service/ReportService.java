package com.example.lms.service;

import com.example.lms.constants.LoanPaymentStatus;
import com.example.lms.entity.Loan;
import com.example.lms.repository.LoanRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public void generateAndSaveLoanReportCSV(String filepath) {
        String csvContent = generateLoanReportCSV();

        Path path = Paths.get(filepath);
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Files.writeString(path, csvContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateCSVForLoan(String loanCategory, Loan loan) {

        return loanCategory + ","
                + loan.getLoanReferenceCode() + ","
                + loan.getPrincipal() + ","
                + loan.getLoanPaymentStatus();
    }

    public void generateAndSaveLoanReportPDF() {
        Map<String, List<Loan>> report = generateLoanReport();
        report.forEach((loanCategory, loans)-> {
            try {
                generateAndSavePDFForLoan(loanCategory, loans, "./reports/"+loanCategory+"_loan_report.pdf");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void generateAndSavePDFForLoan(String category, List<Loan> loans, String filepath) throws IOException {
        try(PdfWriter writer = new PdfWriter(filepath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf)) {
            Table table = new Table(4);
            table.addCell("Category");
            table.addCell("LoanReferenceCode");
            table.addCell("Principal");
            table.addCell("PaymentStatus");
            loans.forEach(loan -> {
                table.addCell(category);
                table.addCell(loan.getLoanReferenceCode());
                table.addCell(loan.getPrincipal().toString());
                table.addCell(loan.getLoanPaymentStatus().toString());
            });
            document.add(table);
            document.add(new Paragraph("\n"));
        }
    }
}
