package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.AccessRefreshTokenDTO;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.services.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AccessRefreshTokenDTO login(Principal principal) {
        if (!(principal instanceof UsernamePasswordAuthenticationToken)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Login is available only by basic authentication");
        }
        User user = (User) ((Authentication) principal).getPrincipal();
        return authService.generateTokens(user);
    }

    @PostMapping("/refresh")
    public AccessRefreshTokenDTO refresh(@RequestBody @Valid RefreshTokenDTO refreshTokenDTO){
        return authService.refreshTokens(refreshTokenDTO);
    }


    @PostMapping("/register")
    public Map<String, String> register(@RequestBody @Valid UserRegisterDTO userRegisterDto) throws MessagingException, UnsupportedEncodingException {
        return authService.registerUser(userRegisterDto);
    }

    @PostMapping("/send-activation-email")
    public void sendActivationEmail(@RequestBody @Valid SendActivationEmailDTO sendActivationEmailDTO) throws MessagingException, UnsupportedEncodingException {
        authService.sendActivationEmail(sendActivationEmailDTO);
    }

    @PostMapping("/forgot")
    public void forgotPassword(@RequestBody @Valid EmailDTO emailDTO) throws MessagingException, UnsupportedEncodingException {
        authService.forgotPassword(emailDTO);
    }

    @PostMapping("/reset")
    public void resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO){
        authService.resetPassword(resetPasswordDTO);
    }

    @PostMapping("/activate")
    public void activate(@RequestBody @Valid ActivationTokenDTO activationTokenDTO) {
        authService.activateUser(activationTokenDTO);
    }
}
