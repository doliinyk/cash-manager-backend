package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDTO(@NotBlank(message = "Title can't be blank")
                          @Size(min = 2, max = 50, message = "Title must be between 2 and 50 characters long")
                          String title) {
}
