package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record UserRegisterDTO(@Size(message = "Login must be between 3 and 30", min = 3, max = 30)
                              @Pattern(regexp = "^(?=.*[a-zA-Z])\\w{3,30}$", message = "Login not valid (please use only a-z0-9_-)")
                              @NotBlank(message = "Login is missing")
                              String login,
                              @Size(min = 8, max = 40, message = "Password must be between 8 and 40")
                              @NotBlank(message = "Password is missing")
                              String password,
                              @Email(message = "Email should be valid")
                              @NotBlank(message = "Email is missing")
                              String email) implements Serializable {
}