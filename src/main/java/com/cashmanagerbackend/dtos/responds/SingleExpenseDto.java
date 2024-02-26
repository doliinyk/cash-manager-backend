package com.cashmanagerbackend.dtos.responds;

import java.io.Serializable;
import java.time.OffsetDateTime;

public record SingleExpenseDto(RespondExpenseCategoryDto category, String description, double cost,
                               OffsetDateTime expensesDate, RespondUserIdDto user) implements Serializable {
}