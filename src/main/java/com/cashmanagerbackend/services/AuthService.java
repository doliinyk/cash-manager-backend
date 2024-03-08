package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.AccessRefreshTokenDTO;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.utils.UserAlreadyExistAuthenticationException;
import jakarta.mail.MessagingException;
import org.springframework.validation.BindingResult;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface AuthService {
    AccessRefreshTokenDTO generateTokens(User user);

    void activateUser(ActivationTokenDTO activationTokenDTO) throws UserAlreadyExistAuthenticationException;

    Map<String, String> registerUser(UserRegisterDto userRegisterDto, BindingResult bindingResult) throws UserAlreadyExistAuthenticationException, MessagingException, UnsupportedEncodingException;

    AccessRefreshTokenDTO refreshTokens(RefreshTokenDTO refreshTokenDTO);

    void sendActivationEmail(SendActivationEmailDTO sendActivationEmailDTO) throws MessagingException, UnsupportedEncodingException;

    void forgotPassword(EmailDTO emailDTO) throws MessagingException, UnsupportedEncodingException;

    void resetPassword(ResetPasswordDTO resetPasswordDTO);
}