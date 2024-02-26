package com.cashmanagerbackend.dtos.responds;

import java.io.Serializable;
import java.time.OffsetDateTime;


public record RespondRegularExpenseDto(long periodicity, String title, String description, double cost,
                                       OffsetDateTime lastPaymentDate, RespondUserIdDto user) implements Serializable {
}