package com.example.lms.utils.business;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "loan.eligibility")
public class LoanEligibilityRules {

    private BigDecimal annualIncomeThreshold;

    private Integer activeLoansThreshold;

    private Integer activeLoanApplicationsThreshold;
}
