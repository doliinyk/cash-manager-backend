package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.EmailDTO;
import com.cashmanagerbackend.dtos.requests.RefreshTokenDTO;
import com.cashmanagerbackend.dtos.requests.ResetPasswordDTO;
import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.dtos.responses.AccessRefreshTokenDTO;
import com.cashmanagerbackend.entities.User;

import java.util.Map;
import java.util.UUID;

public interface AuthService {
    UUID registerUser(UserRegisterDTO userRegisterDTO, String locale, Map<String, Object> variables);

    void activateUser(UUID userId, String activationToken);

    void sendActivationEmail(EmailDTO emailDTO, String locale, Map<String, Object> variables);

    AccessRefreshTokenDTO loginUser(User user);

    AccessRefreshTokenDTO refreshUserTokens(RefreshTokenDTO refreshTokenDTO);

    void forgotPassword(EmailDTO emailDTO, String locale, Map<String, Object> variables);

    void resetPassword(ResetPasswordDTO resetPasswordDTO);
}
