package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.EmailDTO;
import com.cashmanagerbackend.dtos.requests.JWTTokenDTO;
import com.cashmanagerbackend.dtos.requests.ResetPasswordDTO;
import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.dtos.responses.AccessRefreshTokenDTO;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.mappers.UserMapper;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.services.AuthService;
import com.cashmanagerbackend.services.EmailService;
import com.cashmanagerbackend.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this login already exists");
        } else if (userRepository.existsByEmail(userRegisterDTO.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }

        User user = userMapper.dtoToEntity(userRegisterDTO);
        user.setActivated(false);
        user.setActivationRefreshUUID(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.password()));
        user = userRepository.save(user);
        Util.putUserMailVariables(user, variables);

        emailService.sendMail(user.getEmail(), "registration", "registration-mail", variables, locale);

        return user.getId();
    }

    @Override
    @Transactional
    public void activateUser(UUID userId, String activationToken) {
        User user = findUserById(String.valueOf(userId));

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
        User user = findUserByEmail(emailDTO);

        user.setActivationRefreshUUID(UUID.randomUUID());
        Util.putUserMailVariables(user, variables);

        emailService.sendMail(user.getEmail(), "registration", "registration-mail", variables, locale);
    }

    @Override
    @Transactional
    public AccessRefreshTokenDTO loginUser(User user) {
        user = userRepository.save(user);

        return generateTokens(user);
    }

    @Override
    @Transactional
    public AccessRefreshTokenDTO refreshUserTokens(JWTTokenDTO jwtTokenDTO) {
        Jwt refreshTokenJwt = jwtRefreshDecoder.decode(jwtTokenDTO.token());
        User user = findUserById(refreshTokenJwt.getSubject());

        if (!user.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User deleted or not activated");
        }
        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(jwtTokenDTO.token())) {
            throw new JwtException("Provided JWT refresh token doesn't belong to its user");
        }

        return generateTokens(user);
    }

    @Override
    @Transactional
    public void forgotPassword(EmailDTO emailDTO, String locale, Map<String, Object> variables) {
        User user = findUserByEmail(emailDTO);

        user.setActivationRefreshUUID(UUID.randomUUID());
        Util.putUserMailVariables(user, variables);

        emailService.sendMail(user.getEmail(), "forgot", "forgot-mail", variables, locale);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        User user = findUserById(String.valueOf(resetPasswordDTO.id()));

        if (user.getActivationRefreshUUID().equals(resetPasswordDTO.securityCode())) {
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.password()));
            user.setActivationRefreshUUID(null);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong security code");
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
                .expiresAt(now.plus(refreshTokenLifetime, ChronoUnit.DAYS))
                .subject(String.valueOf(user.getId()))
                .claim("scope", scope)
                .build();
        String accessJwt = jwtAccessEncoder.encode(JwtEncoderParameters.from(accessClaims)).getTokenValue();
        String refreshJwt = jwtRefreshEncoder.encode(JwtEncoderParameters.from(refreshClaims)).getTokenValue();
        user.setRefreshToken(refreshJwt);

        return new AccessRefreshTokenDTO(accessJwt, refreshJwt);
    }

    private User findUserById(String id) {
        return userRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this ID doesn't exist")
        );
    }

    private User findUserByEmail(EmailDTO emailDTO) {
        return userRepository.findByEmail(emailDTO.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                               "User with this email doesn't exist"));
    }
}