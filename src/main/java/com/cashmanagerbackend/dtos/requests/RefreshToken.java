package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RefreshToken(@NotBlank(message = "refresh token is blank") @NotNull(message = "refresh token is null") String refreshToken) {
}
