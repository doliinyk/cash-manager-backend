package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.SingleExpenseResponseDTO;
import com.cashmanagerbackend.dtos.responses.SingleIncomeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;

public interface SingleIncomeService {
    SingleIncomeResponseDTO postSingleIncomes(String id, AddSingleIncomeDTO addSingleIncomeDTO);

    SingleIncomeResponseDTO patchSingleIncomes(String id, PatchSingleIncomeDTO patchSingleIncomeDTO);

    void deleteSingleIncomes(String id, DeleteSingleExpenseIncomeDTO deleteSingleExpenseIncomeDTO);

    Page<SingleIncomeResponseDTO> getSingleIncomes(String id, Pageable pageable, OffsetDateTime fromByDate, OffsetDateTime toByDate, String description, Double fromBySize, Double toBySize, String categoryTitle);
}
