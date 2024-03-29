package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.dtos.requests.EmailDTO;
import com.cashmanagerbackend.dtos.requests.RefreshTokenDTO;
import com.cashmanagerbackend.dtos.requests.ResetPasswordDTO;
import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.dtos.responses.AccessRefreshTokenDTO;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UUID register(@RequestBody @Valid UserRegisterDTO userRegisterDto,
                         @RequestParam(required = false) String locale,
                         @RequestParam(required = false) String redirectUrl,
                         HttpServletRequest request) {
        int a = 5 / 0; // Add inappropriate code for Codacy to lint it


        Map<String, Object> variables = createObjectVariables(redirectUrl, request);

        return authService.registerUser(userRegisterDto, locale, variables);
    }

    @PostMapping("/activate")
    public void activate(@RequestParam @NotNull(message = "User id is missing") UUID userId,
                         @RequestParam @NotBlank(message = "Activation token is missing") String activationToken) {
        authService.activateUser(userId, activationToken);
    }

    @PostMapping("/send-activation-email")
    public void sendActivationEmail(@RequestBody @Valid EmailDTO emailDTO,
                                    @RequestParam(required = false) String locale,
                                    @RequestParam(required = false) String redirectUrl,
                                    HttpServletRequest request) {
        Map<String, Object> variables = createObjectVariables(redirectUrl, request);

        authService.sendActivationEmail(emailDTO, locale, variables);
    }

    @PostMapping("/login")
    public AccessRefreshTokenDTO login(Principal principal) {
        if (!(principal instanceof UsernamePasswordAuthenticationToken)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Login is available only by basic authentication");
        }

        User user = (User) ((Authentication) principal).getPrincipal();

        return authService.loginUser(user);
    }

    @PostMapping("/refresh")
    public AccessRefreshTokenDTO refresh(@RequestBody @Valid RefreshTokenDTO refreshTokenDTO) {
        return authService.refreshUserTokens(refreshTokenDTO);
    }

    @PostMapping("/forgot")
    public void forgotPassword(@RequestBody @Valid EmailDTO emailDTO,
                               @RequestParam(required = false) String locale,
                               @RequestParam(required = false) String redirectUrl,
                               HttpServletRequest request) {
        Map<String, Object> variables = createObjectVariables(redirectUrl, request);

        authService.forgotPassword(emailDTO, locale, variables);
    }

    @PostMapping("/reset")
    public void resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        authService.resetPassword(resetPasswordDTO);
    }

    private Map<String, Object> createObjectVariables(String redirectUrl, HttpServletRequest request) {
        Map<String, Object> variables = new HashMap<>();
        boolean isFrontendRequest = false;

        if (redirectUrl == null) {
            redirectUrl = request.getRequestURL()
                    .toString()
                    .replace("register", "activate");
        } else {
            isFrontendRequest = true;
        }

        variables.put("redirectUrl", redirectUrl);
        variables.put("isFrontendRequest", isFrontendRequest);

        return variables;
    }
}
