package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UserUpdateDTO(@Pattern(regexp = "^(?=.*[a-zA-Z])\\w{3,30}$",
        message = "Login must contain at least one letter and can contain numbers or underscore from 3 to 30 symbols")
                            String login,
                            @Email(message = "Email should be valid")
                            String email) {
}
