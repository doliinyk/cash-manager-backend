package com.cashmanagerbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * DTO for {@link com.cashmanagerbackend.entities.User}
 */
@Value
public class UserDto implements Serializable {
    @Size(message = "Login must be between 8 and 20", min = 8, max = 20)
    @NotBlank(message = "Login is missing")
    String login;
    @NotBlank(message = "Password is missing")
    String password;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is missing")
    String email;
    OffsetDateTime createDate;
    double account;
}