package com.cashmanagerbackend.dtos.requests;

import com.cashmanagerbackend.dtos.responses.CategoryResponseDTO;
import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;

public record PatchSingleExpenseIncomeDTO(@NotBlank
                                          String id,
                                          @Size(max = 500, message = "Description length can't be more than 500 characters long")
                                          String description,
                                          @DecimalMin(value = "0.0", message = "Profit must be bigger than 0")
                                          @DecimalMax(Double.MAX_VALUE + "")
                                          double profit,
                                          OffsetDateTime incomeDate,
                                          CategoryResponseDTO category
) {
}
