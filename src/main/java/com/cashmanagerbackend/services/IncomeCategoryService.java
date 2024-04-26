package com.cashmanagerbackend.services;

import com.cashmanagerbackend.dtos.requests.AddCategoryRequestDTO;
import com.cashmanagerbackend.dtos.requests.DeleteCategoryRequestDTO;
import com.cashmanagerbackend.dtos.requests.PatchCategoryRequestDTO;
import com.cashmanagerbackend.dtos.responses.CategoryResponseDTO;

import java.util.Map;

public interface IncomeCategoryService {
    Map<String, CategoryResponseDTO> getUserIncomesCategory(String name);

    Map<String, CategoryResponseDTO> postUserIncomesCategory(String name, AddCategoryRequestDTO addCategoryRequestDTO);

    Map<String, CategoryResponseDTO> patchUserIncomesCategory(String name, PatchCategoryRequestDTO patchCategoryRequestDTO);

    void deleteUserIncomesCategory(String name, DeleteCategoryRequestDTO deleteCategoryRequestDTO);
}
