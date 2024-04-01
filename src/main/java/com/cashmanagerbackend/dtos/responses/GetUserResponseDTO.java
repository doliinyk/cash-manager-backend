package com.cashmanagerbackend.dtos.responses;

import java.time.LocalDateTime;

public record GetUserResponseDTO(String login, String email, LocalDateTime createDate, double account) {
}
