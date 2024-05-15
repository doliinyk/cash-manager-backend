package com.cashmanagerbackend.mappers;

import com.cashmanagerbackend.dtos.requests.AddRegularExpensesDTO;
import com.cashmanagerbackend.dtos.requests.PatchRegularExpensesDTO;
import com.cashmanagerbackend.dtos.responses.RegularExpenseResponseDTO;
import com.cashmanagerbackend.entities.RegularExpense;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RegularExpensesMapper {
    RegularExpenseResponseDTO entityToDto(RegularExpense regularExpense);
    RegularExpense dtoToEntity(AddRegularExpensesDTO addRegularExpensesDTO);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PatchRegularExpensesDTO patchRegularExpensesDTO, @MappingTarget RegularExpense regularExpense);
}
