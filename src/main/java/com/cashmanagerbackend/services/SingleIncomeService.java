package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.SingleIncomeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SingleIncomeService {
    Page<SingleIncomeResponseDTO> getSingleIncomes(String id, Pageable pageable);

    SingleIncomeResponseDTO postSingleIncomes(String id, AddSingleIncomeDTO addSingleIncomeDTO);

    SingleIncomeResponseDTO patchSingleIncomes(String id, PatchSingleIncomeDTO patchSingleIncomeDTO);

    void deleteSingleIncomes(String id, DeleteSingleExpenseIncomeDTO deleteSingleExpenseIncomeDTO);

    Page<SingleIncomeResponseDTO> getSingleIncomesByIncomeDate(String id, Pageable pageable, RangeDatesDTO rangeDatesDTO);

    Page<SingleIncomeResponseDTO> getSingleIncomesByDescription(String id, Pageable pageable, DescriptionDTO descriptionDTO);

    Page<SingleIncomeResponseDTO> getSingleIncomesByCategory(String id, Pageable pageable, CategoryDTO categoryDTO);

    Page<SingleIncomeResponseDTO> getSingleIncomesBySize(String id, Pageable pageable, SizeDTO sizeDTO);
}
