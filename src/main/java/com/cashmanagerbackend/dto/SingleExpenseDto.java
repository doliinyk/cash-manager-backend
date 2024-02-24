package com.cashmanagerbackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * DTO for {@link com.cashmanagerbackend.entities.SingleExpense}
 */
@Value
public class SingleExpenseDto implements Serializable {
    @Size(message = "Description length can't be more than 500", max = 500)
    String description;
    double cost;
    @NotNull
    OffsetDateTime expensesDate;
}