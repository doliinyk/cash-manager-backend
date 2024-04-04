package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(@Size(min = 3, max = 30, message = "Login must be between 3 and 30")
                            String login,
                            @Email(message = "Email should be valid")
                            String email) {
}
