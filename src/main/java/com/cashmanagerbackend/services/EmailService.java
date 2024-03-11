package com.cashmanagerbackend.services;

import com.cashmanagerbackend.entities.User;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailService {
    void sendForgotPasswordMail(User user) throws MessagingException, UnsupportedEncodingException;
    void sendRegistrationConfirmationMail(User user) throws MessagingException, UnsupportedEncodingException;
}
