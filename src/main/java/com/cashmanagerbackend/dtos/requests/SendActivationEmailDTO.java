package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SendActivationEmailDTO(@NotNull(message = "id is missing") UUID id) {
}
