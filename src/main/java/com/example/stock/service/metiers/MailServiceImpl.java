package com.example.stock.service.metiers;

import jakarta.mail.MessagingException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;


@Service
public class MailServiceImpl {
    @Autowired
     JavaMailSender emailSender;

    public String sendSimpleMessageToNewUser(String to, String subject, String text)
            throws MessagingException {

        Properties props = new Properties();
        SimpleMailMessage message = new SimpleMailMessage();
        props.put("mail.smtp.ssl.trust", "*");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

       emailSender.send(message);

        return "email sended";
    }
}
