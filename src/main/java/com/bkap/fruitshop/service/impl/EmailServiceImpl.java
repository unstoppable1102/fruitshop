package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendEmail(String to, String subject, String content) {
        // Gửi email (có thể dùng JavaMailSender hoặc dịch vụ email khác)
        System.out.println("Gửi email đến: " + to);
        System.out.println("Tiêu đề: " + subject);
        System.out.println("Nội dung: " + content);
    }
}
