package com.cashmanagerbackend.dtos.requests;

import com.cashmanagerbackend.dtos.responses.CategoryResponseDTO;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.OffsetDateTime;

public record AddRegularExpensesDTO(@Positive(message = "Periodicity must be positive")
                                    long periodicity,
                                    @Size(message = "Title must be between 2 and 50 characters long", min = 2, max = 50)
                                    @NotBlank(message = "Title can't be blank")
                                    String title,
                                    @Size(message = "Description length can't be more than 500 characters long", max = 500)
                                    String description,
                                    @Positive(message = "Cost must be positive")
                                    double cost,
                                    @NotNull(message = "Last payment date can`t be null")
                                    OffsetDateTime lastPaymentDate,
                                    @NotNull(message = "Category can't be null")
                                    CategoryResponseDTO category) {
}