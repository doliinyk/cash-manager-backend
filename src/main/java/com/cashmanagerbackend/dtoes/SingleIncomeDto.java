package com.cashmanagerbackend.dtoes;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * DTO for {@link com.cashmanagerbackend.entities.SingleIncome}
 */
@Value
public class SingleIncomeDto implements Serializable {
    @Size(message = "Description length can't be more than 500", max = 500)
    String description;
    double profit;
    @NotNull
    OffsetDateTime incomeDate;
}