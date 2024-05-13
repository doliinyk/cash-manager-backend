package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.dtos.requests.AddRegularExpensesDTO;
import com.cashmanagerbackend.dtos.requests.DeletePayment;
import com.cashmanagerbackend.dtos.requests.PatchRegularExpensesDTO;
import com.cashmanagerbackend.dtos.responses.RegularExpenseResponseDTO;
import com.cashmanagerbackend.services.RegularExpensesServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/regular-payments/expenses")
@RequiredArgsConstructor
public class RegularExpensesController {
    private final RegularExpensesServices regularExpensesServices;

    @GetMapping
    public Page<RegularExpenseResponseDTO> getRegularExpenses(Principal principal, @PageableDefault(sort = "title") Pageable pageable){
        return regularExpensesServices.getRegularExpenses(principal.getName(), pageable);
    }

    @PostMapping
    public RegularExpenseResponseDTO postRegularExpenses(Principal principal, @RequestBody @Valid AddRegularExpensesDTO addRegularExpensesDTO){
        return regularExpensesServices.postRegularExpenses(principal.getName(), addRegularExpensesDTO);
    }

    @PatchMapping
    public RegularExpenseResponseDTO patchRegularExpenses(Principal principal, @RequestBody @Valid PatchRegularExpensesDTO patchRegularExpensesDTO){
        return regularExpensesServices.patchRegularExpenses(principal.getName(), patchRegularExpensesDTO);
    }

    @DeleteMapping
    public RegularExpenseResponseDTO deleteRegularExpenses(Principal principal, @RequestBody @Valid DeletePayment deletePayment){
        return regularExpensesServices.deleteRegularExpenses(principal.getName(), deletePayment);
    }
}
