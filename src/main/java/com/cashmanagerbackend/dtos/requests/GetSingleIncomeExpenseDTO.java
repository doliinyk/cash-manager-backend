package com.cashmanagerbackend.dtos.requests;

import java.time.OffsetDateTime;

public record GetSingleIncomeExpenseDTO (OffsetDateTime from,
                                         OffsetDateTime to){
}
