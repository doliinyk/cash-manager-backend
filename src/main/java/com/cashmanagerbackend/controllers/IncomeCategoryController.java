package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.dtos.requests.AddCategoryRequestDTO;
import com.cashmanagerbackend.dtos.requests.DeleteCategoryRequestDTO;
import com.cashmanagerbackend.dtos.requests.GetUserCategoryByIdDTO;
import com.cashmanagerbackend.dtos.requests.PatchCategoryRequestDTO;
import com.cashmanagerbackend.dtos.responses.CategoryResponseDTO;
import com.cashmanagerbackend.services.IncomeCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/categories/incomes")
@RequiredArgsConstructor
public class IncomeCategoryController {
    private final IncomeCategoryService incomeCategoryService;

    @GetMapping
    public Map<String, CategoryResponseDTO> getUserIncomesCategory(Principal principal){
        return incomeCategoryService.getUserIncomesCategory(principal.getName());
    }

    @GetMapping("/by-title")
    public CategoryResponseDTO getUserExspensesCategoryById(Principal principal, @RequestBody @Valid GetUserCategoryByIdDTO getUserCategoryByIdDTO){
        return incomeCategoryService.getUserIncomesCategoryByTitle(principal.getName(), getUserCategoryByIdDTO.title());
    }

    @PostMapping
    public Map<String, CategoryResponseDTO> postUserIncomesCategory
            (Principal principal, @RequestBody @Valid AddCategoryRequestDTO addCategoryRequestDTO){
        return incomeCategoryService.postUserIncomesCategory(principal.getName(), addCategoryRequestDTO);
    }

    @PatchMapping
    public Map<String, CategoryResponseDTO> patchUserIncomesCategory
            (Principal principal, @RequestBody @Valid PatchCategoryRequestDTO patchCategoryRequestDTO){
        return incomeCategoryService.patchUserIncomesCategory(principal.getName(), patchCategoryRequestDTO);
    }

    @DeleteMapping
    public void deleteUserIncomesCategory(Principal principal,
                                            @RequestBody @Valid DeleteCategoryRequestDTO deleteCategoryRequestDTO){
        incomeCategoryService.deleteUserIncomesCategory(principal.getName(), deleteCategoryRequestDTO);
    }
}
