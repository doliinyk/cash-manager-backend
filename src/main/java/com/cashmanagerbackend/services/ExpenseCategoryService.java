package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.AddCategoryRequestDTO;
import com.cashmanagerbackend.dtos.requests.DeleteCategoryRequestDTO;
import com.cashmanagerbackend.dtos.requests.PatchCategoryRequestDTO;
import com.cashmanagerbackend.dtos.responses.CategoryResponseDTO;

import java.util.Map;

public interface ExpenseCategoryService {
    Map<String, CategoryResponseDTO> getUserExspensesCategory(String name);

    Map<String, CategoryResponseDTO> postUserExspensesCategory(String name, AddCategoryRequestDTO addCategoryRequestDTO);

    Map<String, CategoryResponseDTO> patchUserExspensesCategory(String name, PatchCategoryRequestDTO patchCategoryRequestDTO);

    void deleteUserExspensesCategory(String name, DeleteCategoryRequestDTO deleteCategoryRequestDTO);

    CategoryResponseDTO getUserExspensesCategoryByTitle(String userId, String title);
}
