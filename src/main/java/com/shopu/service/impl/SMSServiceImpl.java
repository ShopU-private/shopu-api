package com.shopu.service.impl;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.entities.SMS;
import com.shopu.repository.SMSRepository;
import com.shopu.service.SMSService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import static com.shopu.common.utils.EmailUtil.getHtmlTemplate;
import static com.shopu.common.utils.EmailUtil.getPlainTextTemplate;

@Service
public class SMSServiceImpl implements SMSService {

    @Autowired
    private SMSRepository smsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public ApiResponse<String> sendOtp(String phoneNumber) {
        /// TODO here logic is still pending for sent OTP on Mobile.
        /// Currently i sent OTP on my Gmail

        try{
            String otp = String.valueOf(1000 + new Random().nextInt(9999));
            SMS sms = new SMS(phoneNumber, passwordEncoder.encode(otp)); // email is phoneNumber
            String sessionId = smsRepository.save(sms).getId();

            String htmlBody = getHtmlTemplate(otp);
            String plainTextBody = getPlainTextTemplate(otp);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("gdadvertising2020@gmail.com", "ShopU");
            helper.setReplyTo("gdadvertising2020@gmail.com");
            helper.setTo("ayushverma7463@gmail.com");
            helper.setSubject("Your Verification Code");
            helper.setText(plainTextBody, htmlBody); // both plain text and HTML
            mailSender.send(message);

            return new ApiResponse<>(sessionId,  HttpStatus.OK, "OTP successfully Sent");
        } catch (MessagingException | UnsupportedEncodingException e) {
            return new ApiResponse<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public SMS findById(String smsId) {
        return smsRepository.findById(smsId).orElse(null);
    }

    @Override
    public void delete(String smsId) {
        SMS sms = findById(smsId);
        smsRepository.delete(sms);
    }
}
