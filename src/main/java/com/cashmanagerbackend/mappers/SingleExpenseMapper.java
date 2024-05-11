package com.cashmanagerbackend.mappers;

import com.cashmanagerbackend.dtos.requests.AddSingleExpenseDTO;
import com.cashmanagerbackend.dtos.requests.PatchSingleExpenseDTO;
import com.cashmanagerbackend.dtos.responses.SingleExpenseResponseDTO;
import com.cashmanagerbackend.entities.SingleExpense;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SingleExpenseMapper {
    SingleExpenseResponseDTO entityToDTO(SingleExpense singleExpense);
    SingleExpense dtoToEntity(AddSingleExpenseDTO addSingleExpenseDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PatchSingleExpenseDTO patchSingleExpenseDTO, @MappingTarget SingleExpense singleExpense);
}
