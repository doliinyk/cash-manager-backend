package com.cashmanagerbackend.dtos.responses;

import java.io.Serializable;
import java.time.OffsetDateTime;

public record SingleExpenseDtoResponse(ExpenseCategoryDtoResponse category, String description, double cost, OffsetDateTime expensesDate, UserIdDtoResponse user) implements Serializable {
}