package com.cashmanagerbackend.dtos.responses;

import java.time.OffsetDateTime;

public record SingleIncomeResponseDTO(String id, String description, double profit, OffsetDateTime incomeDate,
                                      CategoryResponseDTO category) {
}
