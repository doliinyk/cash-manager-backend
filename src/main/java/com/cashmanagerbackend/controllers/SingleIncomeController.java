package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.SingleIncomeResponseDTO;
import com.cashmanagerbackend.services.SingleIncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/single-payments/incomes")
@RequiredArgsConstructor
public class SingleIncomeController {
    private final SingleIncomeService singleIncomeService;

    @GetMapping
    public Page<SingleIncomeResponseDTO> getSingleIncomes(Principal principal, @RequestParam(required = false) Optional<Integer> page,
                                                          @RequestParam(required = false) Optional<String> sortBy){
        return singleIncomeService.getSingleIncomes(principal.getName(), page, sortBy);
    }

    @GetMapping("/by-date")
    public Page<SingleIncomeResponseDTO> getSingleIncomesByIncomeDate(Principal principal, @RequestParam(required = false) Optional<Integer> page,
                                                                      @RequestParam(required = false) Optional<String> sortBy, @RequestBody RangeDatesDTO rangeDatesDTO) {
        return singleIncomeService.getSingleIncomesByIncomeDate(principal.getName(), page, sortBy, rangeDatesDTO);
    }

    @GetMapping("/by-description")
    public Page<SingleIncomeResponseDTO> getSingleIncomesByDescription(Principal principal, @RequestParam(required = false) Optional<Integer> page,
                                                                             @RequestParam(required = false) Optional<String> sortBy, @RequestBody DescriptionDTO descriptionDTO){
        return singleIncomeService.getSingleIncomesByDescription(principal.getName(), page, sortBy, descriptionDTO);
    }

    @PostMapping
    public SingleIncomeResponseDTO postSingleIncomes(Principal principal, @RequestBody @Valid AddSingleExpenseIncomeDTO addSingleExpenseIncomeDTO){
        return singleIncomeService.postSingleIncomes(principal.getName(), addSingleExpenseIncomeDTO);
    }

    @PatchMapping
    public SingleIncomeResponseDTO patchSingleIncomes(@RequestBody @Valid PatchSingleExpenseIncomeDTO patchSingleExpenseIncomeDTO){
        return singleIncomeService.patchSingleIncomes(patchSingleExpenseIncomeDTO);
    }

    @DeleteMapping
    public void deleteSingleIncomes(@RequestBody @Valid DeleteSingleExpenseIncomeDTO deleteSingleExpenseIncomeDTO){
        singleIncomeService.deleteSingleIncomes(deleteSingleExpenseIncomeDTO);
    }
}
