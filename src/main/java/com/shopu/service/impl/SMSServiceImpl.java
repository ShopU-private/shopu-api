package com.shopu.service.impl;

import com.shopu.exception.ApplicationException;
import com.shopu.model.entities.SMS;
import com.shopu.repository.SMSRepository;
import com.shopu.service.SMSService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;
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
    public SMS findById(String smsId) {
        return smsRepository.findById(smsId).orElse(null);
    }

    @Override
    public Map<String, String> createOtp(String phoneNumber){
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        SMS sms = new SMS(phoneNumber, passwordEncoder.encode(otp)); // email is phoneNumber
        String sessionId = smsRepository.save(sms).getId();
        return Map.of(
         "otp", otp,
         "sessionId", sessionId
        );
    }

    @Override
    public void delete(String smsId) {
        SMS sms = findById(smsId);
        smsRepository.delete(sms);
    }

    @Override
    @Async("taskExecutor")
    public void sendOtp(String otp){
        try{
            String htmlBody = getHtmlTemplate(otp);
            String plainTextBody = getPlainTextTemplate(otp);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("liveshopu@gmail.com", "ShopU");
            helper.setReplyTo("liveshopu@gmail.com");
            helper.setTo("liveshopu@gmail.com");
            helper.setSubject("Your Login OTP Code");
            helper.setText(plainTextBody, htmlBody); // both plain text and HTML
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new ApplicationException(e.getMessage());
        }
    }
}
