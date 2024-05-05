package com.cashmanagerbackend.dtos.requests;

import java.time.OffsetDateTime;

public record RangeDatesDTO(OffsetDateTime from, OffsetDateTime  to) {
}
