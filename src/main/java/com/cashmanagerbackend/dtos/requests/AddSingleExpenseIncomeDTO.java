package com.cashmanagerbackend.dtos.requests;

import com.cashmanagerbackend.dtos.responses.CategoryResponseDTO;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public record AddSingleExpenseIncomeDTO(
        @Size(max = 500, message = "Description length can't be more than 500 characters long")
        String description,

        @DecimalMin(value = "0.0", message = "Profit must be bigger than 0")
        @DecimalMax(Double.MAX_VALUE + "")
        double profit,

        @NotNull(message = "Income date can't be null")
        OffsetDateTime incomeDate,
        
        @NotNull(message = "Category can't be null")
        CategoryResponseDTO category) {
}
