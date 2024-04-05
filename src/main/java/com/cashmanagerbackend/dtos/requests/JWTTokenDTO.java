package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record JWTTokenDTO(@NotBlank(message = "Refresh token is missing")
                          String token) {
}
