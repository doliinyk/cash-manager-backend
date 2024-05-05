package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.SingleIncomeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SingleIncomeService {
    Page<SingleIncomeResponseDTO> getSingleIncomes(String id, Pageable pageable);

    SingleIncomeResponseDTO postSingleIncomes(String id, AddSingleExpenseIncomeDTO addSingleExpenseIncomeDTO);

    SingleIncomeResponseDTO patchSingleIncomes(String id, PatchSingleExpenseIncomeDTO patchSingleExpenseIncomeDTO);

    void deleteSingleIncomes(String id, DeleteSingleExpenseIncomeDTO deleteSingleExpenseIncomeDTO);

    Page<SingleIncomeResponseDTO> getSingleIncomesByIncomeDate(String id, Pageable pageable, RangeDatesDTO rangeDatesDTO);

    Page<SingleIncomeResponseDTO> getSingleIncomesByDescription(String id, Pageable pageable, DescriptionDTO descriptionDTO);
}
