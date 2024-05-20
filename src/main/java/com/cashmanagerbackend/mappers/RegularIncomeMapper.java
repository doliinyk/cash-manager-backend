package com.cashmanagerbackend.mappers;

import com.cashmanagerbackend.dtos.requests.AddRegularIncomeDTO;
import com.cashmanagerbackend.dtos.requests.PatchRegularIncomeDTO;
import com.cashmanagerbackend.dtos.responses.RegularIncomeResponseDTO;
import com.cashmanagerbackend.entities.RegularIncome;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RegularIncomeMapper {
    RegularIncome dtoToEntity(AddRegularIncomeDTO addRegularIncomeDTO);

    RegularIncomeResponseDTO entityToDto(RegularIncome regularIncome);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PatchRegularIncomeDTO patchRegularIncomeDTO, @MappingTarget RegularIncome regularIncome);
}
