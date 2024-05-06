package com.cashmanagerbackend.dtos.requests;

import com.cashmanagerbackend.dtos.responses.CategoryResponseDTO;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public record AddSingleExpenseDTO(
        @Size(max = 500, message = "Description length can't be more than 500 characters long")
        String description,

        @DecimalMin(value = "0.0", message = "Cost must be bigger than 0")
        @DecimalMax(Double.MAX_VALUE + "")
        double cost,

        @NotNull(message = "Expense date can't be null")
        OffsetDateTime expensesDate,

        @NotNull(message = "Category can't be null")
        CategoryResponseDTO category) {
}
