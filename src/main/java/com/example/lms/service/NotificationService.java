package com.example.lms.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void notifyCustomer(String message) {
        System.out.println("notify customer..."+message);
    }


}
