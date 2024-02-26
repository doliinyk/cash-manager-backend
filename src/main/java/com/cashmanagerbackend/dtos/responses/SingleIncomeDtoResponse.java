package com.cashmanagerbackend.dtos.responses;

import java.io.Serializable;
import java.time.OffsetDateTime;

public record SingleIncomeDtoResponse(IncomeCategoryDtoResponse category, String description, double profit, OffsetDateTime incomeDate, UserIdDtoResponse user) implements Serializable {
}