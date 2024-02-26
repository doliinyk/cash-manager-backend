package com.cashmanagerbackend.dtos.responds;

import java.io.Serializable;
import java.time.OffsetDateTime;


public record RespondRegularIncomeDto(long periodicity, String title, String description,
                                      double profit, OffsetDateTime lastPaymentDate,
                                      RespondUserIdDto user) implements Serializable {
}