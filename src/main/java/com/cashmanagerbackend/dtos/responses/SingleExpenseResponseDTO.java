package com.cashmanagerbackend.dtos.responses;

import java.time.OffsetDateTime;

public record SingleExpenseResponseDTO(String id, String description, double cost, OffsetDateTime expensesDate,
                                       CategoryResponseDTO category) {
}
