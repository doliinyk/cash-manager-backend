package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.EmailDTO;
import com.cashmanagerbackend.dtos.requests.JWTTokenDTO;
import com.cashmanagerbackend.dtos.requests.ResetPasswordDTO;
import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.dtos.responses.AccessRefreshTokenDTO;
import com.cashmanagerbackend.entities.User;

import java.util.Map;
import java.util.UUID;

public interface AuthService {
    UUID registerUser(UserRegisterDTO userRegisterDTO, Map<String, Object> variables, String locale);

    void activateUser(UUID userId, String activationToken);

    void sendActivationEmail(EmailDTO emailDTO, Map<String, Object> variables, String locale);

    AccessRefreshTokenDTO loginUser(User user);

    AccessRefreshTokenDTO refreshUserTokens(JWTTokenDTO jwtTokenDTO);

    void forgotPassword(EmailDTO emailDTO, Map<String, Object> variables, String locale);

    void resetPassword(ResetPasswordDTO resetPasswordDTO);
}
