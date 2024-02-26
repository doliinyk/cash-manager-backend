package com.cashmanagerbackend.dtos.responses;

import java.io.Serializable;
import java.time.OffsetDateTime;


public record RegularIncomeDtoResponse(long periodicity, String title, String description, double profit, OffsetDateTime lastPaymentDate, UserIdDtoResponse user) implements Serializable {
}