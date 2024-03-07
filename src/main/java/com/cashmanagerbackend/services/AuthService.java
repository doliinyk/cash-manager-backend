package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.RefreshToken;
import com.cashmanagerbackend.dtos.responses.AccessRefreshTokenDTO;
import com.cashmanagerbackend.dtos.requests.ActivationTokenDTO;
import com.cashmanagerbackend.dtos.requests.UserRegisterDto;
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

    AccessRefreshTokenDTO refreshTokens(RefreshToken refreshToken);
}
