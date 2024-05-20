package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.RegularExpenseResponseDTO;
import com.cashmanagerbackend.dtos.responses.RegularIncomeResponseDTO;
import com.cashmanagerbackend.services.RegularIncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/regular-payments/incomes")
@RequiredArgsConstructor
public class RegularIncomeController {
    private final RegularIncomeService regularIncomeService;
    @GetMapping
    public Page<RegularIncomeResponseDTO> getRegularIncome(Principal principal, @PageableDefault(sort = "title") Pageable pageable){
        return regularIncomeService.getRegularIncome(principal.getName(), pageable);
    }

    @PostMapping
    public RegularIncomeResponseDTO postRegularExpenses(Principal principal, @RequestBody @Valid AddRegularIncomeDTO addRegularIncomeDTO){
        return regularIncomeService.postRegularIncome(principal.getName(), addRegularIncomeDTO);
    }

    @PatchMapping
    public RegularIncomeResponseDTO patchRegularExpenses(Principal principal, @RequestBody @Valid PatchRegularIncomeDTO patchRegularIncomeDTO){
        return regularIncomeService.patchRegularIncome(principal.getName(), patchRegularIncomeDTO);
    }

    @DeleteMapping
    public RegularIncomeResponseDTO deleteRegularExpenses(Principal principal, @RequestBody @Valid DeletePayment deletePayment){
        return regularIncomeService.deleteRegularIncome(principal.getName(), deletePayment);
    }
}
