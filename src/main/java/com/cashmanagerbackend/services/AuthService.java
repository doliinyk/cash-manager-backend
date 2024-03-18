package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.AccessRefreshTokenDTO;
import com.cashmanagerbackend.entities.User;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface AuthService {
    AccessRefreshTokenDTO generateTokens(User user);

    void activateUser(ActivationTokenDTO activationTokenDTO) ;

    Map<String, String> registerUser(UserRegisterDTO userRegisterDto) throws MessagingException, UnsupportedEncodingException;

    AccessRefreshTokenDTO refreshTokens(RefreshTokenDTO refreshTokenDTO);

    void sendActivationEmail(SendActivationEmailDTO sendActivationEmailDTO) throws MessagingException, UnsupportedEncodingException;

    void forgotPassword(EmailDTO emailDTO) throws MessagingException, UnsupportedEncodingException;

    void resetPassword(ResetPasswordDTO resetPasswordDTO);
}
