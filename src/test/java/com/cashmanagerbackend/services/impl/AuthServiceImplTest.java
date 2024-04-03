package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.mappers.UserMapper;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setup() {
    }

    @Test
    void testRegisterUser() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("login", "password", "email@email.com");
        User user = new User();
        user.setEmail("email@email.com");

        Map<String, Object> variables = new HashMap<>();

        when(userRepository.existsByLogin(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        when(userMapper.dtoToEntity(userRegisterDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        authService.registerUser(userRegisterDTO, "en", variables);

        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendMail(anyString(), anyString(), anyString(), anyMap(), anyString());
    }
}