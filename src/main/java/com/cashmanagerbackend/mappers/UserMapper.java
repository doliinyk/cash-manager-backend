package com.cashmanagerbackend.mappers;

import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.dtos.requests.UserUpdateDTO;
import com.cashmanagerbackend.dtos.responses.UserResponseDTO;
import com.cashmanagerbackend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User dtoToEntity(UserRegisterDTO userRegisterDTO);
    UserResponseDTO entityToDTO(User user);
    void updateEntityFromDto(UserUpdateDTO userUpdateDTO, @MappingTarget User user);
}
