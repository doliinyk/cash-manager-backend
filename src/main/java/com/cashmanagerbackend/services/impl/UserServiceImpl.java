package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.UserPasswordUpdateDTO;
import com.cashmanagerbackend.dtos.requests.UserUpdateDTO;
import com.cashmanagerbackend.dtos.responses.GetUserResponseDTO;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.mappers.UserMapper;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.services.EmailService;
import com.cashmanagerbackend.services.UserService;
import com.cashmanagerbackend.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public GetUserResponseDTO getUser(String id) {
        return userMapper.entityToDTO(findUserById(id));
    }

    @Override
    @Transactional
    public void patchUser(String name, UserUpdateDTO userUpdateDTO, String locale, Map<String, Object> variables) {
        User user = findUserById(name);
        userMapper.updateEntityFromDto(userUpdateDTO, user);
        
        if (!user.getEmail().equals(userUpdateDTO.email()) && userUpdateDTO.email() != null) {
            user.setActivated(false);
            user.setActivationRefreshUUID(UUID.randomUUID());
            Util.putUserMailVariables(user, variables);
            emailService.sendMail(user.getEmail(), "registration", "registration-mail",
                    variables, locale);
        }
    }

    @Override
    @Transactional
    public void deleteUser(String name) {
        findUserById(name).setDeleteDate(Instant.now());
    }

    @Override
    @Transactional
    public void patchUserPassword(String name, UserPasswordUpdateDTO userPasswordUpdateDTO) throws ResponseStatusException {
        User user = findUserById(name);

        if (passwordEncoder.matches(userPasswordUpdateDTO.oldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(userPasswordUpdateDTO.newPassword()));
        } else {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Wrong old password");
        }
    }


    private User findUserById(String id) {
        return userRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResponseStatusException(HttpStatusCode.valueOf(400), "User with this ID doesn't exist")
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLoginOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + username));
    }
}
