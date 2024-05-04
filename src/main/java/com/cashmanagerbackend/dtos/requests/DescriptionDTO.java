package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DescriptionDTO(@NotBlank(message = "Description is missing")
                             @Size(max = 500, message = "Description length can't be more than 500 characters long")
                             String description) {
}
