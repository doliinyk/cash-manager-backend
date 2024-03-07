package com.cashmanagerbackend.dtos.requests;

import com.cashmanagerbackend.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record UserRegisterDto(
        @Size(message = "Login must be between 3 and 30", min = 3, max = 30) @NotBlank(message = "Login is missing") String login,
        @Size(min = 8, max = 20, message = "Password must be between 8 and 30") @NotBlank(message = "Password is missing") String password,
        @Email(message = "Email should be valid") @NotBlank(message = "Email is missing") String email) implements Serializable {

    public static User dtoToEntity(UserRegisterDto userRegisterDto) {
        User user = new User();
        user.setLogin(userRegisterDto.login());
        user.setPassword(userRegisterDto.password());
        user.setEmail(userRegisterDto.email());
        return user;
    }
}