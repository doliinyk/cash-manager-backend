package com.cashmanagerbackend.mappers;

import com.cashmanagerbackend.dtos.responses.CategoryResponseDTO;
import com.cashmanagerbackend.entities.IncomeCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IncomeCategoryMapper {
    CategoryResponseDTO entityToDTO(IncomeCategory incomeCategory);
}
