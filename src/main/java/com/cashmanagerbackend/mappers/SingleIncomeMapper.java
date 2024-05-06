package com.cashmanagerbackend.mappers;

import com.cashmanagerbackend.dtos.requests.AddSingleIncomeDTO;
import com.cashmanagerbackend.dtos.requests.PatchSingleIncomeDTO;
import com.cashmanagerbackend.dtos.responses.SingleIncomeResponseDTO;
import com.cashmanagerbackend.entities.SingleIncome;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SingleIncomeMapper {
    SingleIncomeResponseDTO entityToDTO(SingleIncome singleIncome);
    SingleIncome dtoToEntity(AddSingleIncomeDTO addSingleIncomeDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PatchSingleIncomeDTO patchSingleIncomeDTO, @MappingTarget SingleIncome singleIncome);
}
