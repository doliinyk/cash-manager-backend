package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.SingleExpenseResponseDTO;
import com.cashmanagerbackend.dtos.responses.SingleIncomeResponseDTO;
import com.cashmanagerbackend.services.SingleIncomeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/v1/single-payments/incomes")
@RequiredArgsConstructor
public class SingleIncomeController {
    private final SingleIncomeService singleIncomeService;

    @GetMapping
    public Page<SingleIncomeResponseDTO> getSingleIncomes(Principal principal, @PageableDefault Pageable pageable,
                                                                         @RequestParam(required = false) OffsetDateTime fromByDate,
                                                                         @RequestParam(required = false) OffsetDateTime toByDate,
                                                                         @Size(max = 500, message = "Description length can't be more than 500 characters long")
                                                                         @RequestParam(required = false) String description,
                                                                         @RequestParam(required = false) Double fromBySize,
                                                                         @RequestParam(required = false) Double toBySize,
                                                                         @Size(min = 2, max = 50, message = "Title must be between 2 and 50 characters long")
                                                                         @RequestParam(required = false) String categoryTitle) {
        return singleIncomeService.getSingleIncomes(principal.getName(), pageable, fromByDate, toByDate ,description, fromBySize, toBySize, categoryTitle);
    }

    @PostMapping
    public SingleIncomeResponseDTO postSingleIncomes(Principal principal, @RequestBody @Valid AddSingleIncomeDTO addSingleIncomeDTO){
        return singleIncomeService.postSingleIncomes(principal.getName(), addSingleIncomeDTO);
    }

    @PatchMapping
    public SingleIncomeResponseDTO patchSingleIncomes(Principal principal, @RequestBody @Valid PatchSingleIncomeDTO patchSingleIncomeDTO){
        return singleIncomeService.patchSingleIncomes(principal.getName(), patchSingleIncomeDTO);
    }

    @DeleteMapping
    public void deleteSingleIncomes(Principal principal, @RequestBody @Valid DeleteSingleExpenseIncomeDTO deleteSingleExpenseIncomeDTO){
        singleIncomeService.deleteSingleIncomes(principal.getName(), deleteSingleExpenseIncomeDTO);
    }
}
