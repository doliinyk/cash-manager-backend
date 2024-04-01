package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.UserPasswordUpdateDTO;
import com.cashmanagerbackend.dtos.requests.UserUpdateDTO;
import com.cashmanagerbackend.dtos.responses.UserResponseDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public interface UserService extends UserDetailsService {
    UserResponseDTO getUser(String token);

    void patchUser(String id, UserUpdateDTO userUpdateDTO, String locale, Map<String, Object> variables);

    void deleteUser(String id);

    void patchUserPassword(String id, UserPasswordUpdateDTO userPasswordUpdateDTO) throws ResponseStatusException;
}
