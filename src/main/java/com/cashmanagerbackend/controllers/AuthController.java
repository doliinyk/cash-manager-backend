package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.AccessRefreshTokenDTO;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.services.AuthService;
import com.cashmanagerbackend.utils.UserAlreadyExistAuthenticationException;
import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @PostMapping("/login")
    public AccessRefreshTokenDTO login(Principal principal) {
        log.info("Token requested for user :{}");

        if (!(principal instanceof UsernamePasswordAuthenticationToken)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User " + principal.getName() + " already logged in");
        }
        User user = (User) ((Authentication) principal).getPrincipal();
        return authService.generateTokens(user);
    }

    @PostMapping("/refresh")
    public AccessRefreshTokenDTO refresh(@RequestBody @Valid RefreshTokenDTO refreshTokenDTO){
        return authService.refreshTokens(refreshTokenDTO);
    }


    @PostMapping("/register")
    public Map<String, String> register(@RequestBody @Valid UserRegisterDto userRegisterDto, BindingResult bindingResult) throws MessagingException, UnsupportedEncodingException {
        return authService.registerUser(userRegisterDto, bindingResult);
    }

    @PostMapping("/send-activation-email")
    public void sendActivationEmail(@RequestBody @Valid SendActivationEmailDTO sendActivationEmailDTO, BindingResult bindingResult) throws MessagingException, UnsupportedEncodingException {
        authService.sendActivationEmail(sendActivationEmailDTO);
    }

    @PostMapping("/forgot")
    public void forgotPassword(@RequestBody @Valid EmailDTO emailDTO, BindingResult bindingResult) throws MessagingException, UnsupportedEncodingException {
        authService.forgotPassword(emailDTO);
    }

    @PostMapping("/reset")
    public void resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO, BindingResult bindingResult){
        authService.resetPassword(resetPasswordDTO);
    }

    @PostMapping("/activate")
    public void activate(@RequestBody @Valid ActivationTokenDTO activationTokenDTO, BindingResult bindingResult) {
        authService.activateUser(activationTokenDTO);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleValidationExceptions(
            ConstraintViolationException ex) {

        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("timestamp", LocalDateTime.now().toString());
        errors.put("status", "400");
        errors.put("error", "Bad request");
        ex.getConstraintViolations()
                .forEach(constraintViolation -> errors.put("message", constraintViolation.getMessage()));
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistAuthenticationException.class)
    public Map<String, String> handleUserAlreadyExistAuthenticationExceptions(
            UserAlreadyExistAuthenticationException ex) {

        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("timestamp", LocalDateTime.now().toString());
        errors.put("status", "400");
        errors.put("error", "Bad request");
        errors.put("message", ex.getMessage());
        return errors;
    }
}
