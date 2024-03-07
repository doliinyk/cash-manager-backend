package com.cashmanagerbackend.services.impl;

import com.auth0.jwt.JWT;
import com.cashmanagerbackend.dtos.requests.ActivationTokenDTO;
import com.cashmanagerbackend.dtos.requests.RefreshToken;
import com.cashmanagerbackend.dtos.requests.UserRegisterDto;
import com.cashmanagerbackend.dtos.responses.AccessRefreshTokenDTO;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.services.AuthService;
import com.cashmanagerbackend.utils.UserAlreadyExistAuthenticationException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${auth-service-property.access-token-lifetime}")
    private int accessTokenLifetime;
    @Value("${auth-service-property.refresh-token-lifetime}")
    private int refreshTokenLifetime;
    @Value("${auth-service-property.isuer}")
    private String isuer;
    @Value("${EMAIL}")
    private String email;
    private final JwtEncoder jwtAccessEncoder;
    @Qualifier("refreshEncoder")
    private final JwtEncoder jwtRefreshEncoder;
    @Qualifier("refreshDecoder")
    private final JwtDecoder jwtRefreshDecoder;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;

    public AuthServiceImpl(JwtEncoder jwtAccessEncoder, JwtEncoder jwtRefreshEncoder, JwtDecoder jwtRefreshDecoder, PasswordEncoder passwordEncoder, UserRepository userRepository, JavaMailSender javaMailSender) {
        this.jwtAccessEncoder = jwtAccessEncoder;
        this.jwtRefreshEncoder = jwtRefreshEncoder;
        this.jwtRefreshDecoder = jwtRefreshDecoder;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
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
        User user1 = userRepository.findByLogin(user.getLogin()).get();
        user1.setRefreshToken(refreshJwt);
        userRepository.save(user1);
        return new AccessRefreshTokenDTO(accessJwt, refreshJwt);
    }

    @Override
    public void activateUser(ActivationTokenDTO activationTokenDTO) throws UserAlreadyExistAuthenticationException {
        User user = userRepository.findById(activationTokenDTO.userId()).orElseThrow(() ->
                 new UserAlreadyExistAuthenticationException("user with this id don't exist"));
        if (user.getActivationUUID().equals(UUID.fromString(activationTokenDTO.activationToken()))){
            user.setActivated(true);
            userRepository.save(user);
        }else {
            throw new UserAlreadyExistAuthenticationException("wrong activation token");
        }
    }

    @Override
    public Map<String,String> registerUser(UserRegisterDto userRegisterDto, BindingResult bindingResult) throws MessagingException, UnsupportedEncodingException {
        if (userRepository.existsByLogin(userRegisterDto.login())){
            throw new UserAlreadyExistAuthenticationException("User with this login already exist");
        }else if (userRepository.existsByEmail(userRegisterDto.email())){
            throw new UserAlreadyExistAuthenticationException("User with this email already exist");
        }
        User user = UserRegisterDto.dtoToEntity(userRegisterDto);
        user.setActivated(false);
        user.setActivationUUID(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(userRegisterDto.password()));
        userRepository.save(user);
        sendRegistrationConfirmationMail(user);
        Map<String,String> map = new HashMap<>();
        map.put("userId",userRepository.findByLogin(userRegisterDto.login()).get().getId().toString());
        return map;
    }

    @Override
    public AccessRefreshTokenDTO refreshTokens(RefreshToken refreshToken) {
        Jwt refreshTokenJwt = jwtRefreshDecoder.decode(refreshToken.refreshToken());
        User user = userRepository.findById(UUID.fromString(refreshTokenJwt.getSubject())).get();

        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken.refreshToken())) {
            throw new JwtException("Provided Jwt refresh token doesn't belong to its user");
        }

        return generateTokens(user);
    }

    private void sendRegistrationConfirmationMail(User user) throws MessagingException, UnsupportedEncodingException {
        String senderName = "CashManager";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please check code below to verify your registration:<br>"
                + "<h3>" + user.getActivationUUID() + "</h3>"
                + "Thank you,<br>"
                + senderName + ".";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setFrom(email, senderName);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getLogin());
        helper.setText(content, true);
        javaMailSender.send(message);


//            String senderName = "CashManager";
//    String subject = "Please verify your registration";
//    String content = "Dear [[name]],<br>"
//            + "Please click the link below to verify your registration:<br>"
//            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
//            + "Thank you,<br>"
//            + senderName + ".";
//
//    MimeMessage message = javaMailSender.createMimeMessage();
//    MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setTo(user.getEmail());
//        helper.setFrom(email, senderName);
//        helper.setSubject(subject);
//    content = content.replace("[[name]]", user.getLogin());
//    String verifyURL = "http://localhost:8080" + "/api/v1/auth/activate/?userId=" + user.getId() +
//            "&activationToken=" + user.getActivationUUID();
//    content = content.replace("[[URL]]", verifyURL);
    }




}