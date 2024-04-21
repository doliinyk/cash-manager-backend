package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.AddCategoryRequestDTO;
import com.cashmanagerbackend.dtos.responses.CategoryResponseDTO;

import java.util.Map;

public interface ExpenseCategoryService {
    Map<String, CategoryResponseDTO> getUserExspensesCategory(String name);

    Map<String, CategoryResponseDTO> postUserExspensesCategory(String name, AddCategoryRequestDTO addCategoryRequestDTO);
}
