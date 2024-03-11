package com.cashmanagerbackend.mappers;

import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
    User dtoToEntity(UserRegisterDTO userRegisterDTO);
}
