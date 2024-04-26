package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record AddCategoryRequestDTO(@NotBlank(message = "Category title is missing") String title,
                                    @NotBlank(message = "Category color is missing") String colorCode) {
}
