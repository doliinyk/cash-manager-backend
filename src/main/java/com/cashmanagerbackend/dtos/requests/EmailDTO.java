package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDTO(@Email(message = "Email should be valid") @NotBlank(message = "Email is missing") String email) {
}
