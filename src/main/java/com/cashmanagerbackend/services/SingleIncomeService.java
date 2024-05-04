package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.SingleIncomeResponseDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface SingleIncomeService {
    Page<SingleIncomeResponseDTO> getSingleIncomes(String id, Optional<Integer> page, Optional<String> sortBy);

    SingleIncomeResponseDTO postSingleIncomes(String id, AddSingleExpenseIncomeDTO addSingleExpenseIncomeDTO);

    SingleIncomeResponseDTO patchSingleIncomes(PatchSingleExpenseIncomeDTO patchSingleExpenseIncomeDTO);

    void deleteSingleIncomes(DeleteSingleExpenseIncomeDTO deleteSingleExpenseIncomeDTO);

    Page<SingleIncomeResponseDTO> getSingleIncomesByIncomeDate(String id, Optional<Integer> page, Optional<String> sortBy, RangeDatesDTO rangeDatesDTO);

    Page<SingleIncomeResponseDTO> getSingleIncomesByDescription(String id, Optional<Integer> page, Optional<String> sortBy, DescriptionDTO descriptionDTO);
}
