package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record DeleteSingleExpenseIncomeDTO(@NotBlank String id) {
}
