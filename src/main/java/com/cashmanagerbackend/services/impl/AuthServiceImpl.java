package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.AccessRefreshTokenDTO;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.mappers.UserMapper;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.services.AuthService;
import com.cashmanagerbackend.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${cashmanager.security.jwt.access-token-minutes-amount}")
    private int accessTokenLifetime;
    @Value("${cashmanager.security.jwt.refresh-token-minutes-amount}")
    private int refreshTokenLifetime;
    @Value("${cashmanager.security.jwt.issuer}")
    private String isuer;

    private final JwtEncoder jwtAccessEncoder;
    private final JwtEncoder jwtRefreshEncoder;
    private final JwtDecoder jwtRefreshDecoder;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserMapper userMapper;

    public AuthServiceImpl(JwtEncoder jwtAccessEncoder, @Qualifier("refreshEncoder") JwtEncoder jwtRefreshEncoder, @Qualifier("refreshDecoder") JwtDecoder jwtRefreshDecoder, PasswordEncoder passwordEncoder, UserRepository userRepository, EmailService emailService, UserMapper userMapper) {
        this.jwtAccessEncoder = jwtAccessEncoder;
        this.jwtRefreshEncoder = jwtRefreshEncoder;
        this.jwtRefreshDecoder = jwtRefreshDecoder;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public AccessRefreshTokenDTO generateTokens(User user) {
        Instant now = Instant.now();

        String scope = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet accessClaims = JwtClaimsSet.builder()
                .issuer(isuer)
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenLifetime, ChronoUnit.MINUTES))
                .subject(String.valueOf(user.getId()))
                .claim("scope", scope)
                .build();
        JwtClaimsSet refreshClaims = JwtClaimsSet.builder()
                .issuer(isuer)
                .issuedAt(now)
                .expiresAt(now.plus(refreshTokenLifetime, ChronoUnit.MINUTES))
                .subject(String.valueOf(user.getId()))
                .claim("scope", scope)
                .build();
        String accessJwt = jwtAccessEncoder.encode(JwtEncoderParameters.from(accessClaims)).getTokenValue();
        String refreshJwt = jwtRefreshEncoder.encode(JwtEncoderParameters.from(refreshClaims)).getTokenValue();
        user.setRefreshToken(refreshJwt);
        return new AccessRefreshTokenDTO(accessJwt, refreshJwt);
    }

    @Override
    @Transactional
    public void activateUser(ActivationTokenDTO activationTokenDTO) {
        User user = userRepository.findById(activationTokenDTO.userId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatusCode.valueOf(400), "user with this id don't exist"));
        if (user.getActivationRefreshUUID().equals(UUID.fromString(activationTokenDTO.activationToken()))) {
            user.setActivated(true);
            user.setActivationRefreshUUID(null);
        } else {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Wrong activation token");
        }
    }

    @Override
    @Transactional
    public Map<String, String> registerUser(UserRegisterDTO userRegisterDto) throws MessagingException, UnsupportedEncodingException {
        if (userRepository.existsByLogin(userRegisterDto.login())) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "User with this login already exist");
        } else if (userRepository.existsByEmail(userRegisterDto.email())) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "User with this email already exist");
        }

        User user = userMapper.dtoToEntity(userRegisterDto);
        user.setActivated(false);
        user.setActivationRefreshUUID(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(userRegisterDto.password()));

        emailService.sendRegistrationConfirmationMail(user);

        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(user.getId()));
        return map;
    }

    @Override
    public AccessRefreshTokenDTO refreshTokens(RefreshTokenDTO refreshTokenDTO) {
        Jwt refreshTokenJwt = jwtRefreshDecoder.decode(refreshTokenDTO.refreshToken());
        User user = userRepository.findById(UUID.fromString(refreshTokenJwt.getSubject())).orElseThrow(
                () -> new ResponseStatusException(HttpStatusCode.valueOf(400), "User with this ID doesn't exist")
        );

        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshTokenDTO.refreshToken())) {
            throw new JwtException("Provided Jwt refresh token doesn't belong to its user");
        }

        return generateTokens(user);
    }

    @Override
    @Transactional
    public void sendActivationEmail(SendActivationEmailDTO sendActivationEmailDTO) throws MessagingException, UnsupportedEncodingException {
        User user = userRepository.findByEmail(sendActivationEmailDTO.email()).orElseThrow(
                () -> new ResponseStatusException(HttpStatusCode.valueOf(400), "User with this email doesn't exist")
        );
        user.setActivationRefreshUUID(UUID.randomUUID());
        emailService.sendRegistrationConfirmationMail(user);
    }

    @Override
    @Transactional
    public void forgotPassword(EmailDTO emailDTO) throws MessagingException, UnsupportedEncodingException {
        User user = userRepository.findByEmail(emailDTO.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(400), "User with this email doesn't exist"));
        user.setActivationRefreshUUID(UUID.randomUUID());
        emailService.sendForgotPasswordMail(user);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        User user = userRepository.findById(resetPasswordDTO.id()).orElseThrow(
                () -> new ResponseStatusException(HttpStatusCode.valueOf(400), "User with this id doesn't exist"));
        if (user.getActivationRefreshUUID().equals(resetPasswordDTO.securityCode())) {
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.password()));
            user.setActivationRefreshUUID(null);
        } else throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Wrong security code");
    }
}