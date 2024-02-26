package com.cashmanagerbackend.dtos.responses;

import java.io.Serializable;
import java.time.OffsetDateTime;


public record RegularExpenseDtoResponse(long periodicity, String title, String description, double cost, OffsetDateTime lastPaymentDate, UserIdDtoResponse user) implements Serializable {
}