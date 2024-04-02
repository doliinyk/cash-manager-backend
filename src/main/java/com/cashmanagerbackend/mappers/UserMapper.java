package com.cashmanagerbackend.mappers;

import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.dtos.requests.UserUpdateDTO;
import com.cashmanagerbackend.dtos.responses.UserResponseDTO;
import com.cashmanagerbackend.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User dtoToEntity(UserRegisterDTO userRegisterDTO);

    UserResponseDTO entityToDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserUpdateDTO userUpdateDTO, @MappingTarget User user);
}
