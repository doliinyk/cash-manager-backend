package com.cashmanagerbackend.dtos.requests;

import java.util.UUID;

public record ActivationTokenDTO(UUID userId, String activationToken) {
}
