package com.cashmanagerbackend.dtos.responses;

import java.time.OffsetDateTime;

public record RegularIncomeResponseDTO(long periodicity,
                                       String title,
                                       String description,
                                       double profit,
                                       OffsetDateTime lastPaymentDate,
                                       OffsetDateTime createDate,
                                       CategoryResponseDTO category) {
}
