package com.shopu.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopu.exception.ApplicationException;
import com.shopu.model.entities.SMS;
import com.shopu.repository.common.SMSRepository;
import com.shopu.service.SMSService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
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

    @Value("${2factor.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

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
    public SMS findById(String smsId) {
        return smsRepository.findById(smsId).orElse(null);
    }

    @Override
    public void delete(String smsId) {
        SMS sms = findById(smsId);
        smsRepository.delete(sms);
    }

    @Override
    @Async("taskExecutor")
    public void sendMailOtp(String email, String otp){
        try{
            String htmlBody = getHtmlTemplate(otp);
            String plainTextBody = getPlainTextTemplate(otp);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("liveshopu@gmail.com", "ShopU");
            helper.setReplyTo("liveshopu@gmail.com");
            helper.setTo(email);
            helper.setSubject("Your Login OTP Code");
            helper.setText(plainTextBody, htmlBody); // both plain text and HTML
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    @Override
    public String sendSmsOtp(String phoneNumber) {
        try {
            String sendUrl ="https://2factor.in/API/V1/"+apiKey+"/SMS/"+phoneNumber+"/AUTOGEN";
            ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                    sendUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, String>>() {}
            );
            if(response.getBody() != null && "Success".equalsIgnoreCase((String)  response.getBody().get("Status"))){
                /// Details containing Session ID of OTP
                return (String) response.getBody().get("Details");
            }else{
                throw new ApplicationException("Failed to send OTP");
            }
        } catch (Exception e) {
            throw new ApplicationException("SMS sending failed: " + e.getMessage());
        }
    }

    @Override
    public boolean verifySmsOtp(String sessionId, String otp) {
        String verifyOtpUrl ="https://2factor.in/API/V1/"+apiKey+"/SMS/VERIFY/"+sessionId+"/"+otp;
        try{
            ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                    verifyOtpUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            if (response.getBody() != null && "Success".equalsIgnoreCase((String) response.getBody().get("Status"))) {
                return true;
            } else {
                throw new ApplicationException("Something went wrong, Try again");
            }
        } catch (HttpClientErrorException e){
            try{
                String responseBody = e.getResponseBodyAsString();
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> map = mapper.readValue(responseBody, new TypeReference<>() {
                });

                String status = map.get("Status");
                String details = map.get("Details");

                if ("Error".equalsIgnoreCase(status) && "OTP Mismatch".equalsIgnoreCase(details)){
                    return false;
                }else {
                    throw new ApplicationException(details);
                }
        } catch (JsonProcessingException jsonEx){
                throw new ApplicationException("Something went wrong. Try again");
            }
        }
    }
}