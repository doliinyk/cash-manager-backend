package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.AddCategoryRequestDTO;
import com.cashmanagerbackend.dtos.requests.DeleteCategoryRequestDTO;
import com.cashmanagerbackend.dtos.requests.PatchCategoryRequestDTO;
import com.cashmanagerbackend.dtos.responses.CategoryResponseDTO;

import java.util.Map;

public interface IncomeCategoryService {
    Map<String, CategoryResponseDTO> getUserIncomesCategory(String id);

    Map<String, CategoryResponseDTO> postUserIncomesCategory(String id, AddCategoryRequestDTO addCategoryRequestDTO);

    Map<String, CategoryResponseDTO> patchUserIncomesCategory(String id, PatchCategoryRequestDTO patchCategoryRequestDTO);

    void deleteUserIncomesCategory(String name, DeleteCategoryRequestDTO deleteCategoryRequestDTO);

    CategoryResponseDTO getUserIncomesCategoryByTitle(String userId, String title);
}
