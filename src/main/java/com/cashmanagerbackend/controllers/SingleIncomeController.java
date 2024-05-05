package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.SingleIncomeResponseDTO;
import com.cashmanagerbackend.services.SingleIncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/single-payments/incomes")
@RequiredArgsConstructor
public class SingleIncomeController {
    private final SingleIncomeService singleIncomeService;

    @GetMapping
    public Page<SingleIncomeResponseDTO> getSingleIncomes(Principal principal, @PageableDefault Pageable pageable){
        return singleIncomeService.getSingleIncomes(principal.getName(), pageable);
    }

    @GetMapping("/by-date")
    public Page<SingleIncomeResponseDTO> getSingleIncomesByIncomeDate(Principal principal, @PageableDefault Pageable pageable, @RequestBody RangeDatesDTO rangeDatesDTO) {
        return singleIncomeService.getSingleIncomesByIncomeDate(principal.getName(), pageable, rangeDatesDTO);
    }

    @GetMapping("/by-description")
    public Page<SingleIncomeResponseDTO> getSingleIncomesByDescription(Principal principal, @PageableDefault Pageable pageable, @RequestBody DescriptionDTO descriptionDTO){
        return singleIncomeService.getSingleIncomesByDescription(principal.getName(), pageable, descriptionDTO);
    }

    @PostMapping
    public SingleIncomeResponseDTO postSingleIncomes(Principal principal, @RequestBody @Valid AddSingleExpenseIncomeDTO addSingleExpenseIncomeDTO){
        return singleIncomeService.postSingleIncomes(principal.getName(), addSingleExpenseIncomeDTO);
    }

    @PatchMapping
    public SingleIncomeResponseDTO patchSingleIncomes(Principal principal, @RequestBody @Valid PatchSingleExpenseIncomeDTO patchSingleExpenseIncomeDTO){
        return singleIncomeService.patchSingleIncomes(principal.getName(), patchSingleExpenseIncomeDTO);
    }

    @DeleteMapping
    public void deleteSingleIncomes(Principal principal, @RequestBody @Valid DeleteSingleExpenseIncomeDTO deleteSingleExpenseIncomeDTO){
        singleIncomeService.deleteSingleIncomes(principal.getName(), deleteSingleExpenseIncomeDTO);
    }
}
