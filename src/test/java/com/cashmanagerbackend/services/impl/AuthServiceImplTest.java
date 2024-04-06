package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.EmailDTO;
import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.dtos.responses.AccessRefreshTokenDTO;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.mappers.UserMapper;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.services.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SpringBootTest
class AuthServiceImplTest {
    @InjectMocks
    AuthServiceImpl authService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    EmailService emailService;

    @Mock
    JwtEncoder jwtAccessEncoder;

    @Mock
    JwtEncoder jwtRefreshEncoder;

    @Mock
    JwtDecoder jwtRefreshDecoder;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should create and return user's UUID when register user")
    void testRegisterUser() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("login", "password", "email@email.com");
        Map<String, Object> variables = new HashMap<>();
        String locale = "en";

        User user = new User();
        user.setEmail(userRegisterDTO.email());
        setField(user, "id", UUID.randomUUID());

        when(userRepository.existsByLogin(userRegisterDTO.login())).thenReturn(false);
        when(userRepository.existsByEmail(userRegisterDTO.email())).thenReturn(false);
        when(userMapper.dtoToEntity(userRegisterDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        UUID uuid = authService.registerUser(userRegisterDTO, variables, locale);

        assertEquals(user.getId(), uuid);
        verify(userRepository, times(1)).save(user);
        verify(emailService, times(1))
                .sendMail(user.getEmail(), "registration", "registration-mail", variables, locale);
    }

    @Test
    @DisplayName("Should throw ResponseStatusException with conflict status when register user with duplicate login")
    void testRegisterUserWithDuplicateLogin() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("login", "password", "email@email.com");
        Map<String, Object> variables = new HashMap<>();
        String locale = "en";

        when(userRepository.existsByLogin(userRegisterDTO.login())).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authService.registerUser(userRegisterDTO, variables, locale)
        );
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(userRepository, never()).save(any());
        verify(emailService, never()).sendMail(anyString(), anyString(), anyString(), anyMap(), anyString());
    }

    @Test
    @DisplayName("Should throw ResponseStatusException with conflict status when register user with duplicate email")
    void testRegisterUserWithDuplicateEmail() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("login", "password", "email@email.com");
        Map<String, Object> variables = new HashMap<>();
        String locale = "en";

        when(userRepository.existsByEmail(userRegisterDTO.email())).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authService.registerUser(userRegisterDTO, variables, locale)
        );
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(userRepository, never()).save(any());
        verify(emailService, never()).sendMail(anyString(), anyString(), anyString(), anyMap(), anyString());
    }

    @Test
    @DisplayName("Should set user activated and return when activateUser called")
    void testActivateUser() {
        UUID uuid = UUID.randomUUID();
        String activationToken = UUID.randomUUID().toString();

        User user = new User();
        user.setActivationRefreshUUID(UUID.fromString(activationToken));

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        authService.activateUser(uuid, activationToken);

        assertTrue(user.isActivated());
        assertNull(user.getActivationRefreshUUID());
    }

    @Test
    @DisplayName(
            "Should throw ResponseStatusException with not found status when activateUser called and user doesn't exist"
    )
    void testActivateUserWithUserDoesntExist() {
        UUID uuid = UUID.randomUUID();
        String activationToken = UUID.randomUUID().toString();

        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> authService.activateUser(uuid, activationToken));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName(
            "Should throw ResponseStatusException with unauthorized status when activateUser called with wrong token"
    )
    void testActivateUserWithWrongToken() {
        UUID uuid = UUID.randomUUID();
        String activationToken = UUID.randomUUID().toString();

        User user = new User();
        user.setActivationRefreshUUID(UUID.randomUUID());

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> authService.activateUser(uuid, activationToken));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    @DisplayName("Should send mail when sendActivationEmail called")
    void testSendActivationEmail() {
        EmailDTO emailDTO = new EmailDTO("email@email.com");
        Map<String, Object> variables = new HashMap<>();
        String locale = "en";

        User user = new User();
        user.setEmail(emailDTO.email());

        when(userRepository.findByEmail(emailDTO.email())).thenReturn(Optional.of(user));

        authService.sendActivationEmail(emailDTO, variables, locale);

        assertNotNull(user.getActivationRefreshUUID());
        verify(emailService, times(1))
                .sendMail(user.getEmail(), "registration", "registration-mail", variables, locale);
    }

    @Test
    @DisplayName(
            "Should throw ResponseStatusException with not found status when sendActivationEmail called and user doesn't exist"
    )
    void testSendActivationEmailWithUserDoesntExist() {
        EmailDTO emailDTO = new EmailDTO("email@email.com");
        Map<String, Object> variables = new HashMap<>();
        String locale = "en";

        when(userRepository.findByEmail(emailDTO.email())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authService.sendActivationEmail(emailDTO, variables, locale)
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(emailService, never()).sendMail(anyString(), anyString(), anyString(), anyMap(), anyString());
    }

    @Test
    @DisplayName("Should generate and return tokens when loginUser called")
    void testLoginUser() {
        setField(authService, "issuer", "issuer");
        setField(authService, "accessTokenLifetime", 30);
        setField(authService, "refreshTokenLifetime", 2);

        User user = new User();
        Jwt jwt = new Jwt("value",
                          Instant.now(),
                          Instant.now().plusSeconds(30),
                          Map.of("header", "header"),
                          Map.of("claim", "claim"));

        when(userRepository.save(user)).thenReturn(user);
        when(jwtAccessEncoder.encode(any())).thenReturn(jwt);
        when(jwtRefreshEncoder.encode(any())).thenReturn(jwt);

        AccessRefreshTokenDTO accessRefreshTokenDTO = authService.loginUser(user);

        assertNotNull(accessRefreshTokenDTO);
    }
}