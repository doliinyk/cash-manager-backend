package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ActivationTokenDTO(@NotNull(message = "UserId is missing") UUID userId, @NotBlank(message = "Activation token is missing") String activationToken) {
}
