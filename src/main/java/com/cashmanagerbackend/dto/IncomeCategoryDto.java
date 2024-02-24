package com.cashmanagerbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.cashmanagerbackend.entities.IncomeCategory}
 */
@Value
public class IncomeCategoryDto implements Serializable {
    @Size(message = "Title must be between 2 and 50", min = 2, max = 50)
    @NotBlank(message = "Title can't be blank")
    String title;
}