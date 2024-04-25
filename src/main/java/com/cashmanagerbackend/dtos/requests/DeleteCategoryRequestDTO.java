package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record DeleteCategoryRequestDTO(@NotBlank String title) {
}
