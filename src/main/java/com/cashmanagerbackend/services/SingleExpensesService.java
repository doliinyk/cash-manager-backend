package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.SingleExpenseResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;

public interface SingleExpensesService {

    SingleExpenseResponseDTO postSingleExpenses(String id, AddSingleExpenseDTO addSingleExpenseDTO);

    SingleExpenseResponseDTO patchSingleExpenses(String id, PatchSingleExpenseDTO patchSingleExpenseDTO);

    void deleteSingleExpenses(String id, DeleteSingleExpenseIncomeDTO deleteSingleExpenseIncomeDTO);

    Page<SingleExpenseResponseDTO> getSingleExpenses(String id, Pageable pageable, OffsetDateTime fromByDate, OffsetDateTime toByDate, String description, Double fromBySize, Double toBySize, String categoryTitle);
}
