package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UserUpdateDTO(@Pattern(regexp = "^(?=.*[a-zA-Z])\\w{3,30}$",
        message = "Login must be between 3 and 30 characters long and contain at least one letter")
                            String login,
                            @Email(message = "Email should be valid")
                            String email) {
}
