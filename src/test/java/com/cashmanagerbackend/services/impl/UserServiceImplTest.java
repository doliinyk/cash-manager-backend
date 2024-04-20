package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.UserPasswordUpdateDTO;
import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.dtos.requests.UserUpdateDTO;
import com.cashmanagerbackend.dtos.responses.UserResponseDTO;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.mappers.UserMapper;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.services.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SpringBootTest
class UserServiceImplTest {
    @MockBean
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @MockBean
    EmailService emailService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should return data about user like login, email, create date and account in UserResponseDTO")
    void getUser() {
        User user = new User();
        setField(user, "id", UUID.randomUUID());
        user.setEmail("email@email.com");
        user.setLogin("rash");
        user.setCreateDate(LocalDateTime.now());
        user.setAccount(0);

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        UserResponseDTO userResponseDTO = userService.getUser(user.getId().toString());

        assertEquals(user.getEmail(), userResponseDTO.email());
        assertEquals(user.getLogin(), userResponseDTO.login());
        assertEquals(user.getAccount(), userResponseDTO.account());
        assertEquals(user.getCreateDate(), userResponseDTO.createDate());
    }

    @Test
    @DisplayName("Should return data about user who change his email or login this must look like login, email, create date and account in UserResponseDTO")
    void patchUser() {
        UUID uuid = UUID.randomUUID();
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("rash", "email@email.com");
        String locate = "en";
        String redirectUrl = "http://example.com/auth/activate";
        Map<String, Object> variables = new HashMap<>();
        variables.put("redirectUrl", redirectUrl);
        variables.put("isFrontendRequest", true);
        User user = new User();
        setField(user, "id", uuid);
        user.setLogin("bash");
        user.setEmail("gamail@email.com");

        when(userRepository.findById(uuid))
                .thenReturn(Optional.of(user));

        userService.patchUser(String.valueOf(uuid), userUpdateDTO,variables, locate);

        assertEquals(user.getLogin(), userUpdateDTO.login());
        assertEquals(user.getEmail(), userUpdateDTO.email());
    }

    @Test
    @DisplayName("Should set delete date for user")
    void deleteUser() {
        User user = new User();
        setField(user, "id", UUID.randomUUID());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.deleteUser(String.valueOf(user.getId()));

        assertNotNull(user.getDeleteDate());
    }

    @Test
    @DisplayName("Should update user password")
    void patchUserPasswordWithRightOldPassword() {
        UserPasswordUpdateDTO userPasswordUpdateDTO =
                new UserPasswordUpdateDTO("password", "password1");
        User user = new User();
        setField(user, "id", UUID.randomUUID());
        String password = passwordEncoder.encode(userPasswordUpdateDTO.oldPassword());
        user.setPassword(password);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.patchUserPassword(String.valueOf(user.getId()), userPasswordUpdateDTO);

        assertTrue(passwordEncoder.matches(userPasswordUpdateDTO.newPassword(),user.getPassword()));
    }
    @Test
    @DisplayName("Should throw ResponseStatusException when you try change user password wit wrong old password")
    void patchUserPasswordWithWrongOldPassword() {
        UserPasswordUpdateDTO userPasswordUpdateDTO =
                new UserPasswordUpdateDTO("password", "password1");
        User user = new User();
        setField(user, "id", UUID.randomUUID());
        String password = passwordEncoder.encode("oldPassword");
        user.setPassword(password);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(ResponseStatusException.class,() -> userService.patchUserPassword(String.valueOf(user.getId()), userPasswordUpdateDTO));
    }

    @Test
    @DisplayName("Should set null on deleteDate field when restore user")
    void restoreUser() {
        UserRegisterDTO userRegisterDTO =
                new UserRegisterDTO("login", "password", "email@email.com");
        User user = new User();
        setField(user, "id", UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.password()));
        user.setLogin(userRegisterDTO.login());
        user.setEmail(userRegisterDTO.email());
        user.setDeleteDate(Instant.now());


        when(userRepository.findByLoginAndEmail(userRegisterDTO.login(), userRegisterDTO.email())).thenReturn(Optional.of(user));

        userService.restoreUser(userRegisterDTO);

        assertNull(user.getDeleteDate());
    }
}