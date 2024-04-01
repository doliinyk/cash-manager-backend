package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RestoreUserDTO(@Size(min = 3, max = 30, message = "Login must be between 3 and 30")
                             @NotBlank(message = "Login is missing")
                             String login,
                             @Email(message = "Email should be valid")
                             @NotBlank(message = "Email is missing")
                             String email,
                             @Size(min = 8, max = 40, message = "Password must be between 8 and 40")
                             @NotBlank(message = "Old password is missing")
                             String password) {
}
