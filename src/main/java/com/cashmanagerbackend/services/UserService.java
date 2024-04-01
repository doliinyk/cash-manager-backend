package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.UserPasswordUpdateDTO;
import com.cashmanagerbackend.dtos.requests.UserUpdateDTO;
import com.cashmanagerbackend.dtos.responses.GetUserResponseDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public interface UserService extends UserDetailsService {
    GetUserResponseDTO getUser(String token);

    void patchUser(String name, UserUpdateDTO userUpdateDTO, String locale, Map<String, Object> variables);

    void deleteUser(String name);

    void patchUserPassword(String name, UserPasswordUpdateDTO userPasswordUpdateDTO) throws ResponseStatusException;
}
