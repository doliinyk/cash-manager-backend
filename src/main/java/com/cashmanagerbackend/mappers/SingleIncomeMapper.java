package com.cashmanagerbackend.mappers;

import com.cashmanagerbackend.dtos.requests.AddSingleExpenseIncomeDTO;
import com.cashmanagerbackend.dtos.requests.PatchSingleExpenseIncomeDTO;
import com.cashmanagerbackend.dtos.responses.SingleIncomeResponseDTO;
import com.cashmanagerbackend.entities.SingleIncome;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SingleIncomeMapper {
    SingleIncomeResponseDTO entityToDTO(SingleIncome singleIncome);
    SingleIncome dtoToEntity(AddSingleExpenseIncomeDTO addSingleExpenseIncomeDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PatchSingleExpenseIncomeDTO patchSingleExpenseIncomeDTO, @MappingTarget SingleIncome singleIncome);
}
