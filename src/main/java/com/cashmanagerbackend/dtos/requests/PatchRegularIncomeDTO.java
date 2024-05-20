package com.cashmanagerbackend.dtos.requests;

import com.cashmanagerbackend.dtos.responses.CategoryResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public record PatchRegularIncomeDTO(@Positive(message = "Periodicity must be bigger than 0")
                                    Long periodicity,
                                    @Size(message = "Title must be between 2 and 50 characters long", min = 2, max = 50) @NotBlank(message = "Old title can't be blank")
                                    String oldTitle,
                                    @Size(message = "Title must be between 2 and 50 characters long", min = 2, max = 50)
                                    String newTitle,
                                    @Size(message = "Description length can't be more than 500 characters long", max = 500)
                                    String description,
                                    OffsetDateTime lastPaymentDate,
                                    @Positive(message = "Profit must be bigger than 0")
                                    Double profit,
                                    CategoryResponseDTO category) {
}
