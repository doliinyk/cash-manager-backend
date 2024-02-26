package com.cashmanagerbackend.dtos.responses;

import java.io.Serializable;
import java.util.UUID;

public record UserIdDtoResponse(UUID id) implements Serializable {
}