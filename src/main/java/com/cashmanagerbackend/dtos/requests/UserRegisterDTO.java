package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterDTO(@NotBlank(message = "Login is missing")
                              @Pattern(regexp = "^(?=.*[a-zA-Z])\\w{3,30}$",
                                      message = "Login must be between 3 and 30 characters long and contain at least one letter")
                              String login,
                              @NotBlank(message = "Password is missing")
                              @Size(min = 8, max = 40, message = "Password must be between 8 and 40 characters long")
                              String password,
                              @NotBlank(message = "Email is missing")
                              @Email(message = "Email must be valid")
                              String email) {
}