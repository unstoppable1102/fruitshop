package com.bkap.fruitshop.service;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
}
