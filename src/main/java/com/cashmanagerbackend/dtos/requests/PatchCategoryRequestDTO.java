package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record PatchCategoryRequestDTO(@NotBlank
                                      String oldTitle,
                                      String newTitle,
                                      String colorCode) {
}
