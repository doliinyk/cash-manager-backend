package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.UserPasswordUpdateDTO;
import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.dtos.requests.UserUpdateDTO;
import com.cashmanagerbackend.dtos.responses.UserResponseDTO;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.mappers.UserMapper;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.services.EmailService;
import com.cashmanagerbackend.services.UserService;
import com.cashmanagerbackend.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUser(String id) {
        return userMapper.entityToDTO(findUserById(id));
    }

    @Override
    @Transactional
    public UserResponseDTO patchUser(String name, UserUpdateDTO userUpdateDTO, String locale, Map<String, Object> variables) {
        User user = findUserById(name);
        String email = user.getEmail();

        userMapper.updateEntityFromDto(userUpdateDTO, user);

        if (!email.equals(userUpdateDTO.email()) && userUpdateDTO.email() != null) {
            user.setActivated(false);
            user.setActivationRefreshUUID(UUID.randomUUID());
            Util.putUserMailVariables(user, variables);
            emailService.sendMail(user.getEmail(), "registration", "registration-mail",
                    variables, locale);
        }

        return userMapper.entityToDTO(user);
    }

    @Override
    @Transactional
    public void deleteUser(String name) {
        findUserById(name).setDeleteDate(Instant.now());
    }

    @Override
    @Transactional
    public void patchUserPassword(String name, UserPasswordUpdateDTO userPasswordUpdateDTO) {
        User user = findUserById(name);

        if (passwordEncoder.matches(userPasswordUpdateDTO.oldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(userPasswordUpdateDTO.newPassword()));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong old password");
        }
    }

    @Override
    @Transactional
    public void restoreUser(UserRegisterDTO userRegisterDTO) {
        User user = userRepository.findByLoginAndEmail(userRegisterDTO.login(), userRegisterDTO.email()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this login and email not found")
        );
        if (passwordEncoder.matches(userRegisterDTO.password(), user.getPassword())){
            user.setDeleteDate(null);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password");
        }
    }

    private User findUserById(String id) {
        return userRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this ID doesn't exist")
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByLoginOrEmail(username, username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found by username: " + username));
    }
}
