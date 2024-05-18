package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.AddRegularIncomeDTO;
import com.cashmanagerbackend.dtos.requests.DeletePayment;
import com.cashmanagerbackend.dtos.requests.PatchRegularIncomeDTO;
import com.cashmanagerbackend.dtos.responses.RegularIncomeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RegularIncomeService {
    Page<RegularIncomeResponseDTO> getRegularIncome(String id, Pageable pageable);

    RegularIncomeResponseDTO postRegularIncome(String id, AddRegularIncomeDTO addRegularExpensesDTO);

    RegularIncomeResponseDTO patchRegularIncome(String id, PatchRegularIncomeDTO patchRegularExpensesDTO);

    RegularIncomeResponseDTO deleteRegularIncome(String id, DeletePayment deletePayment);
}
