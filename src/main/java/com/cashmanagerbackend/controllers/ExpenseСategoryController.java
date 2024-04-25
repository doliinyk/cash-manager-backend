package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.dtos.requests.AddCategoryRequestDTO;
import com.cashmanagerbackend.dtos.requests.DeleteCategoryRequestDTO;
import com.cashmanagerbackend.dtos.requests.PatchCategoryRequestDTO;
import com.cashmanagerbackend.dtos.responses.CategoryResponseDTO;
import com.cashmanagerbackend.services.ExpenseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/categories/expenses")
@RequiredArgsConstructor
public class ExpenseСategoryController {
    private final ExpenseCategoryService expenseCategoryService;

    @GetMapping
    public Map<String, CategoryResponseDTO> getUserExspensesCategory(Principal principal){
         return expenseCategoryService.getUserExspensesCategory(principal.getName());
    }

    @PostMapping
    public Map<String, CategoryResponseDTO> postUserExspensesCategory
            (Principal principal, @RequestBody AddCategoryRequestDTO addCategoryRequestDTO){
        return expenseCategoryService.postUserExspensesCategory(principal.getName(), addCategoryRequestDTO);
    }

    @PatchMapping
    public Map<String, CategoryResponseDTO> patchUserExspensesCategory
            (Principal principal, @RequestBody PatchCategoryRequestDTO patchCategoryRequestDTO){
        return expenseCategoryService.patchUserExspensesCategory(principal.getName(), patchCategoryRequestDTO);
    }

    @DeleteMapping
    public void deleteUserExspensesCategory(Principal principal,
                                            @RequestBody DeleteCategoryRequestDTO deleteCategoryRequestDTO){
        expenseCategoryService.deleteUserExspensesCategory(principal.getName(), deleteCategoryRequestDTO);
    }
}
