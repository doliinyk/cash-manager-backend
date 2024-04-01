package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JWTTokenDTO(@NotBlank(message = "Refresh token is missing") @NotNull(message = "Refresh token is missing") String token) {
}
