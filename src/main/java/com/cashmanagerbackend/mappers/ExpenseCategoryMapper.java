package com.cashmanagerbackend.mappers;

import com.cashmanagerbackend.dtos.responses.CategoryResponseDTO;
import com.cashmanagerbackend.entities.ExpenseCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExpenseCategoryMapper {
    CategoryResponseDTO entityToDTO(ExpenseCategory expenseCategory);
}
