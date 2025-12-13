package com.example.lms.events;

import com.example.lms.entity.LoanApplication;
import com.example.lms.repository.LoanApplicationRepository;
import com.example.lms.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@AllArgsConstructor
@Component
public class LoanApplicationReviewedEventListener {

    private final LoanApplicationRepository loanApplicationRepository;
    private final NotificationService notificationService;

    @EventListener
    public void handleLoanApplicationReviewedEvent(LoanApplicationReviewedEvent event) {
        LoanApplication loanApplication = event.loanApplication();
        loanApplication.setReviewedAt(LocalDateTime.now());
        loanApplication.setReviewedBy("anonymousReviewer");
        loanApplicationRepository.save(loanApplication);
        notificationService.notifyCustomer("loan application reviewed at "+ loanApplication.getReviewedAt()+" Current status: "+loanApplication.getLoanApplicationStatus());
    }
}
