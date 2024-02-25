package com.cashmanagerbackend.dtoes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * DTO for {@link com.cashmanagerbackend.entities.RegularExpense}
 */
@Value
public class RegularExpenseDto implements Serializable {
    long periodicity;
    @Size(message = "Title must be between 2 and 50", min = 2, max = 50)
    @NotBlank(message = "Title can't be blank")
    String title;
    @Size(message = "Description length can't be more than 500", max = 500)
    String description;
    double cost;
    OffsetDateTime lastPaymentDate;
    OffsetDateTime createDate;
}