package com.cashmanagerbackend.dtos.responses;

import java.time.LocalDateTime;

public record UserResponseDTO(String login, String email, LocalDateTime createDate, double account) {
}
