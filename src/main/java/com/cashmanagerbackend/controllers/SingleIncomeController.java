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

    @GetMapping("/by-size")
    public Page<SingleIncomeResponseDTO> getSingleIncomesBySize(Principal principal, @PageableDefault Pageable pageable, @RequestBody SizeDTO sizeDTO){
        return singleIncomeService.getSingleIncomesBySize(principal.getName(), pageable, sizeDTO);
    }

    @GetMapping("/by-category")
    public  Page<SingleIncomeResponseDTO> getSingleIncomesByCategory(Principal principal, @PageableDefault Pageable pageable, @RequestBody @Valid CategoryDTO categoryDTO){
        return singleIncomeService.getSingleIncomesByCategory(principal.getName(), pageable, categoryDTO);
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
