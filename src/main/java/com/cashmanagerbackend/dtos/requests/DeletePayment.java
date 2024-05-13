package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record DeletePayment(
        @Size(message = "Title must be between 2 and 50 characters long", min = 2, max = 50) @NotBlank(message = "Title can't be blank")
        String title) {
}