package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.SingleExpenseResponseDTO;
import com.cashmanagerbackend.services.SingleExpensesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/v1/single-payments/expenses")
@RequiredArgsConstructor
public class SingleExpensesController {
    private final SingleExpensesService singleExpensesService;

    @GetMapping
    public Page<SingleExpenseResponseDTO> getSingleExpenses(Principal principal, @PageableDefault Pageable pageable,
                                                                         @RequestParam(required = false) OffsetDateTime fromByDate,
                                                                         @RequestParam(required = false) OffsetDateTime toByDate,
                                                                         @Size(max = 500, message = "Description length can't be more than 500 characters long")
                                                                             @RequestParam(required = false) String description,
                                                                         @RequestParam(required = false) Double fromBySize,
                                                                         @RequestParam(required = false) Double toBySize,
                                                                             @Size(min = 2, max = 50, message = "Title must be between 2 and 50 characters long")
                                                                         @RequestParam(required = false) String categoryTitle) {
        return singleExpensesService.getSingleExpenses(principal.getName(), pageable, fromByDate, toByDate ,description, fromBySize, toBySize, categoryTitle);
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