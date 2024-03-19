package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.EmailDTO;
import com.cashmanagerbackend.dtos.requests.RefreshTokenDTO;
import com.cashmanagerbackend.dtos.requests.ResetPasswordDTO;
import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.dtos.responses.AccessRefreshTokenDTO;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.mappers.UserMapper;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.services.AuthService;
import com.cashmanagerbackend.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("${cashmanager.security.jwt.issuer}")
    private String issuer;

    @Value("${cashmanager.security.jwt.access-token-minutes-amount}")
    private int accessTokenLifetime;

    @Value("${cashmanager.security.jwt.refresh-token-minutes-amount}")
    private int refreshTokenLifetime;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final JwtEncoder jwtAccessEncoder;
    @Qualifier("refreshEncoder")
    private final JwtEncoder jwtRefreshEncoder;
    private final JwtDecoder jwtRefreshDecoder;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UUID registerUser(UserRegisterDTO userRegisterDTO, String locale, Map<String, Object> variables) {
        if (userRepository.existsByLogin(userRegisterDTO.login())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this login already exist");
        } else if (userRepository.existsByEmail(userRegisterDTO.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exist");
        }

        User user = userMapper.dtoToEntity(userRegisterDTO);
        user.setActivated(false);
        user.setActivationRefreshUUID(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.password()));
        user = userRepository.save(user);
        putUserMailVariables(user, variables);

        emailService.sendMail(user.getEmail(), "registration", "registration-mail", variables, locale);

        return user.getId();
    }

    @Override
    @Transactional
    public void activateUser(UUID userId, String activationToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User with this id don't exist")
                );

        if (user.getActivationRefreshUUID().equals(UUID.fromString(activationToken))) {
            user.setActivated(true);
            user.setActivationRefreshUUID(null);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong activation token");
        }
    }

    @Override
    @Transactional
    public void sendActivationEmail(EmailDTO emailDTO, String locale, Map<String, Object> variables) {
        User user = userRepository.findByEmail(emailDTO.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(400),
                                                               "User with this email doesn't exist"));

        user.setActivationRefreshUUID(UUID.randomUUID());
        putUserMailVariables(user, variables);

        emailService.sendMail(user.getEmail(), "registration", "registration-mail", variables, locale);
    }

    @Override
    @Transactional
    @SneakyThrows
    public AccessRefreshTokenDTO loginUser(User user) {
        user = userRepository.save(user);

        return generateTokens(user);
    }

    @Override
    @Transactional
    public AccessRefreshTokenDTO refreshUserTokens(RefreshTokenDTO refreshTokenDTO) {
        Jwt refreshTokenJwt = jwtRefreshDecoder.decode(refreshTokenDTO.refreshToken());
        User user = userRepository.findById(UUID.fromString(refreshTokenJwt.getSubject())).orElseThrow(
                () -> new ResponseStatusException(HttpStatusCode.valueOf(400), "User with this ID doesn't exist")
        );

        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshTokenDTO.refreshToken())) {
            throw new JwtException("Provided JWT refresh token doesn't belong to its user");
        }

        return generateTokens(user);
    }

    @Override
    @Transactional
    public void forgotPassword(EmailDTO emailDTO, String locale, Map<String, Object> variables) {
        User user = userRepository.findByEmail(emailDTO.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(400),
                                                               "User with this email doesn't exist"));

        user.setActivationRefreshUUID(UUID.randomUUID());
        putUserMailVariables(user, variables);

        emailService.sendMail(user.getEmail(), "forgot", "forgot-mail", variables, locale);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        User user = userRepository.findById(resetPasswordDTO.id()).orElseThrow(
                () -> new ResponseStatusException(HttpStatusCode.valueOf(400), "User with this id doesn't exist"));

        if (user.getActivationRefreshUUID().equals(resetPasswordDTO.securityCode())) {
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.password()));
            user.setActivationRefreshUUID(null);
        } else {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Wrong security code");
        }
    }

    private AccessRefreshTokenDTO generateTokens(User user) {
        Instant now = Instant.now();

        String scope = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet accessClaims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenLifetime, ChronoUnit.MINUTES))
                .subject(String.valueOf(user.getId()))
                .claim("scope", scope)
                .build();
        JwtClaimsSet refreshClaims = JwtClaimsSet.builder()
                .issuer(issuer)
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

    private void putUserMailVariables(User user, Map<String, Object> variables) {
        variables.put("login", user.getLogin());
        variables.put("redirectUrl",
                      variables.get("redirectUrl") + "?userId=" + user.getId() + "&activationToken=" + user.getActivationRefreshUUID());
    }
}