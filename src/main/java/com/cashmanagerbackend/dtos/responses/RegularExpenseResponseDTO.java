package com.cashmanagerbackend.dtos.responses;

import java.time.OffsetDateTime;
public record RegularExpenseResponseDTO(long periodicity,
                                        String title,
                                        String description,
                                        double cost, OffsetDateTime lastPaymentDate,
                                        OffsetDateTime createDate,
                                        CategoryResponseDTO category) {
}