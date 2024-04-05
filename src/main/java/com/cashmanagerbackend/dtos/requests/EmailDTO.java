package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDTO(@NotBlank(message = "Email is missing")
                       @Email(message = "Email should be valid")
                       String email) {
}
