package ecommerce_website.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("VibeCart Email Verification OTP");
        msg.setText("Your OTP for VibeCart registration is: " + otp);
        mailSender.send(msg);
        System.out.println("Sending OTP to " + to + ": " + otp);
    }
}

