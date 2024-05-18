package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.SingleExpenseResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SingleExpensesService {

    Page<SingleExpenseResponseDTO> getSingleExpenses(String id, Pageable pageable);

    Page<SingleExpenseResponseDTO> getSingleExpensesByExpensesDate(String id, Pageable pageable, RangeDatesDTO rangeDatesDTO);

    Page<SingleExpenseResponseDTO> getSingleExpensesByDescription(String id, Pageable pageable, DescriptionDTO descriptionDTO);

    SingleExpenseResponseDTO postSingleExpenses(String id, AddSingleExpenseDTO addSingleExpenseDTO);

    SingleExpenseResponseDTO patchSingleExpenses(String id, PatchSingleExpenseDTO patchSingleExpenseDTO);

    void deleteSingleExpenses(String id, DeleteSingleExpenseIncomeDTO deleteSingleExpenseIncomeDTO);

    Page<SingleExpenseResponseDTO> getSingleExpensesBySize(String id, Pageable pageable, SizeDTO sizeDTO);

    Page<SingleExpenseResponseDTO> getSingleExpensesByCategory(String id, Pageable pageable, CategoryDTO categoryDTO);
}
