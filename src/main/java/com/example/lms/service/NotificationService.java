package com.example.lms.service;

import com.example.lms.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    public void send(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void notifyCustomer(Customer customer, String subject, String message) {

        send(customer.getEmail(), subject, message);
        log.info("notified customer with \nemail: {}, \nsubject: {}, \nmessage: {}", customer, subject, message);
    }


}
