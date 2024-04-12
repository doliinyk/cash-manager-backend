package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.UserUpdateDTO;
import com.cashmanagerbackend.dtos.responses.UserResponseDTO;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.mappers.UserMapper;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.services.EmailService;
import com.cashmanagerbackend.services.UserService;
import com.cashmanagerbackend.utils.Util;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SpringBootTest
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;
    @Mock
    EmailService emailService;
    @InjectMocks
    UserServiceImpl userService;


    @Test
    void getUser() {
        User user = new User();
        setField(user, "id", UUID.randomUUID());
        user.setEmail("email@email.com");
        user.setLogin("rash");
        user.setCreateDate(LocalDateTime.now());
        user.setAccount(0);

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(userMapper.entityToDTO(user)).thenReturn(
                new UserResponseDTO("rash", "email@email.com",
                user.getCreateDate(), 0));

        UserResponseDTO userResponseDTO = userService.getUser(user.getId().toString());

        assertEquals(user.getEmail(), userResponseDTO.email());
        assertEquals(user.getLogin(), userResponseDTO.login());
        assertEquals(user.getAccount(), userResponseDTO.account());
        assertEquals(user.getCreateDate(), userResponseDTO.createDate());
    }

    @Test
    void patchUser() {
//        UUID uuid = UUID.randomUUID();
//        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("rash", "email@email.com");
//        String locate = "en";
//        String redirectUrl = "http://example.com/auth/activate";
//        Map<String, Object> variables = new HashMap<>();
//        variables.put("redirectUrl", redirectUrl);
//        variables.put("isFrontendRequest", true);
//        User user = new User();
//        setField(user, "id", uuid);
//        user.setLogin("bash");
//        user.setEmail("gamail@email.com");
//
//        when(userRepository.findById(uuid))
//                .thenReturn(Optional.of(user));
//        doCallRealMethod().when(userMapper.updateEntityFromDto(userUpdateDTO, user)).doA;

    }

    @Test
    void deleteUser() {
    }

    @Test
    void patchUserPassword() {
    }

    @Test
    void restoreUser() {
    }

    @Test
    void loadUserByUsername() {
    }
}