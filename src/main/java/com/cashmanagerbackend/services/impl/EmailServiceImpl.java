package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${EMAIL}")
    private String email;
    public void sendForgotPasswordMail(User user) throws MessagingException, UnsupportedEncodingException {
        String senderName = "CashManager";
        String subject = "Need to reset your password?";
        String content = "Hi [[name]],<br>"
                + "There was a request to change your password!<br>"
                + "Use your secret code!<br>"
                + "<h3>" + user.getActivationUUID() + "</h3>"
                + "If you did not make this request then please ignore this email.<br>"
                + senderName + ".";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setFrom(email, senderName);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getLogin());
        helper.setText(content, true);
        javaMailSender.send(message);
    }

    public void sendRegistrationConfirmationMail(User user) throws MessagingException, UnsupportedEncodingException {
        String senderName = "CashManager";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please check code below to verify your registration:<br>"
                + "<h3>" + user.getActivationUUID() + "</h3>"
                + "Thank you,<br>"
                + senderName + ".";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setFrom(email, senderName);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getLogin());
        helper.setText(content, true);
        javaMailSender.send(message);


//            String senderName = "CashManager";
//    String subject = "Please verify your registration";
//    String content = "Dear [[name]],<br>"
//            + "Please click the link below to verify your registration:<br>"
//            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
//            + "Thank you,<br>"
//            + senderName + ".";
//
//    MimeMessage message = javaMailSender.createMimeMessage();
//    MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setTo(user.getEmail());
//        helper.setFrom(email, senderName);
//        helper.setSubject(subject);
//    content = content.replace("[[name]]", user.getLogin());
//    String verifyURL = "http://localhost:8080" + "/api/v1/auth/activate/?userId=" + user.getId() +
//            "&activationToken=" + user.getActivationUUID();
//    content = content.replace("[[URL]]", verifyURL);
    }
}
