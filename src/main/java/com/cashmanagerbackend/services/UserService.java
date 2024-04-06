package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.UserPasswordUpdateDTO;
import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.dtos.requests.UserUpdateDTO;
import com.cashmanagerbackend.dtos.responses.UserResponseDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

public interface UserService extends UserDetailsService {
    UserResponseDTO getUser(String id);

    UserResponseDTO patchUser(String id, UserUpdateDTO userUpdateDTO, Map<String, Object> variables, String locale);

    void deleteUser(String id);

    void patchUserPassword(String id, UserPasswordUpdateDTO userPasswordUpdateDTO);

    void restoreUser(UserRegisterDTO userRegisterDTO);
}
