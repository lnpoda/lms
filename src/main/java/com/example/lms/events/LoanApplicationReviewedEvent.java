package com.example.lms.events;

import com.example.lms.entity.LoanApplication;
import lombok.Getter;

public record LoanApplicationReviewedEvent(LoanApplication loanApplication) {
    //        super(loanApplication);

}
