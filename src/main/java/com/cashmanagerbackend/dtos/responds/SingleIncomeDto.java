package com.cashmanagerbackend.dtos.responds;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * DTO for {@link com.cashmanagerbackend.entities.SingleIncome}
 */
public record SingleIncomeDto(RespondIncomeCategoryDto category, String description, double profit,
                              OffsetDateTime incomeDate, RespondUserIdDto user) implements Serializable {
}