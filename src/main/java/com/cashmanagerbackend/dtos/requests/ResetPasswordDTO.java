package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ResetPasswordDTO(@NotNull(message = "User id is missing") UUID id,
                               @NotNull(message = "Security code is missing") UUID securityCode,
                               @NotBlank(message = "Password is missing")
                               @Size(min = 8, max = 20, message = "Password must be between 8 and 20")
                               String password) {
}
