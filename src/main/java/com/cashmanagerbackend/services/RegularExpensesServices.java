package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.AddRegularExpensesDTO;
import com.cashmanagerbackend.dtos.requests.DeletePayment;
import com.cashmanagerbackend.dtos.requests.PatchRegularExpensesDTO;
import com.cashmanagerbackend.dtos.responses.RegularExpenseResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RegularExpensesServices {
    RegularExpenseResponseDTO postRegularExpenses(String id, AddRegularExpensesDTO addRegularExpensesDTO);

    RegularExpenseResponseDTO patchRegularExpenses(String id, PatchRegularExpensesDTO patchRegularExpensesDTO);

    Page<RegularExpenseResponseDTO> getRegularExpenses(String id, Pageable pageable);

    RegularExpenseResponseDTO deleteRegularExpenses(String id, DeletePayment deletePayment);
}
