package com.cashmanagerbackend.mappers;

import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.dtos.responses.GetUserResponseDTO;
import com.cashmanagerbackend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User dtoToEntity(UserRegisterDTO userRegisterDTO);
    GetUserResponseDTO entityToDTO(User user);
}
