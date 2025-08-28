package com.shopu.service.impl;

import com.shopu.exception.ApplicationException;
import com.shopu.model.entities.SMS;
import com.shopu.repository.SMSRepository;
import com.shopu.service.SMSService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
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

    @Value("${fast2sms.api.key}")
    private String apiKey;

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

    @Override
    @Async("taskExecutor")
    public void sendSMS(String otp, String phoneNumber) {
        try {
            String message = "Dear Customer, " + otp + " is your one time password (OTP) for phone verification.";

            String data = "sender_id=TXTIND"
                    + "&message=" + URLEncoder.encode(message, "UTF-8")
                    + "&route=v3"
                    + "&numbers=" + phoneNumber;

            URL url = new URL("https://www.fast2sms.com/dev/bulkV2");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", apiKey);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();

            // Read response
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            System.out.println("Fast2SMS Response: " + response);

            connection.disconnect();
        } catch (Exception e) {
            throw new ApplicationException("SMS sending failed: " + e.getMessage());
        }
    }
}
