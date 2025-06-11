package com.shopu.service.impl;

import com.shopu.common.utils.ApiResponse;
import com.shopu.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;


    public ApiResponse<String> sendOtp(String email) {
        try {
            int otp = 1000 + new Random().nextInt(9999);


            String htmlBody = getHtmlTemplate(otp + "");
            String plainTextBody = getPlainTextTemplate(otp + "");

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("gdadvertising2020@gmail.com", "Coding Age");
            helper.setReplyTo("gdadvertising2020@gmail.com");
            helper.setTo(email);
            helper.setSubject("Your Coding Age Verification Code");
            helper.setText(plainTextBody, htmlBody); // both plain text and HTML
            mailSender.send(message);
            return new ApiResponse<>(otp + "", HttpStatus.OK);
        } catch (MessagingException | UnsupportedEncodingException e) {
            return new ApiResponse<>(null, HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private String getHtmlTemplate(String otp) {
        return """
                <!DOCTYPE html>
                <html>
                <body style="font-family: sans-serif; padding: 10px;">
                  <h2>ShopU Verification Code</h2>
                  <p>Your OTP is: <strong>%s</strong></p>
                  <p>This code is valid for 10 minutes. Please do not share it.</p>
                  <p>Thank you,<br>ShopU Team</p>
                </body>
                </html>
                """.formatted(otp);
    }


    private String getPlainTextTemplate(String otp) {
        return """
                ShopU Verification Code
                
                Your OTP is: %s
                
                This code is valid for 10 minutes. Please do not share it.
                
                Thank you,
                ShopU Team
                """.formatted(otp);
    }

}
