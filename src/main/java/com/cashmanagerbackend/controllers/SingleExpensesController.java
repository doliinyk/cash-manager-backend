package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.SingleExpenseResponseDTO;
import com.cashmanagerbackend.services.SingleExpensesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/single-payments/expenses")
@RequiredArgsConstructor
public class SingleExpensesController {
    private final SingleExpensesService singleExpensesService;

    @GetMapping
    public Page<SingleExpenseResponseDTO> getSingleExpenses(Principal principal, @PageableDefault Pageable pageable){
        return singleExpensesService.getSingleExpenses(principal.getName(), pageable);
    }

    @GetMapping("/by-date")
    public Page<SingleExpenseResponseDTO> getSingleExpensesByExpenseDate(Principal principal, @PageableDefault Pageable pageable, @RequestBody RangeDatesDTO rangeDatesDTO) {
        return singleExpensesService.getSingleExpensesByExpensesDate(principal.getName(), pageable, rangeDatesDTO);
    }

    @GetMapping("/by-description")
    public Page<SingleExpenseResponseDTO> getSingleExpensesByDescription(Principal principal, @PageableDefault Pageable pageable, @RequestBody @Valid DescriptionDTO descriptionDTO){
        return singleExpensesService.getSingleExpensesByDescription(principal.getName(), pageable, descriptionDTO);
    }

    @GetMapping("/by-size")
    public Page<SingleExpenseResponseDTO> getSingleExpensesBySize(Principal principal, @PageableDefault Pageable pageable, @RequestBody SizeDTO sizeDTO){
        return singleExpensesService.getSingleExpensesBySize(principal.getName(), pageable, sizeDTO);
    }

    @GetMapping("/by-category")
    public  Page<SingleExpenseResponseDTO> getSingleExpensesByCategory(Principal principal, @PageableDefault Pageable pageable, @RequestBody @Valid CategoryDTO categoryDTO){
        return singleExpensesService.getSingleExpensesByCategory(principal.getName(), pageable, categoryDTO);
    }

    @PostMapping
    public SingleExpenseResponseDTO postSingleExpenses(Principal principal, @RequestBody @Valid AddSingleExpenseDTO addSingleExpenseDTO){
        return singleExpensesService.postSingleExpenses(principal.getName(), addSingleExpenseDTO);
    }

    @PatchMapping
    public SingleExpenseResponseDTO patchSingleExpenses(Principal principal, @RequestBody @Valid PatchSingleExpenseDTO patchSingleExpenseDTO){
        return singleExpensesService.patchSingleExpenses(principal.getName(), patchSingleExpenseDTO);
    }

    @DeleteMapping
    public void deleteSingleExpenses(Principal principal, @RequestBody @Valid DeleteSingleExpenseIncomeDTO deleteSingleExpenseIncomeDTO){
        singleExpensesService.deleteSingleExpenses(principal.getName(), deleteSingleExpenseIncomeDTO);
    }
}