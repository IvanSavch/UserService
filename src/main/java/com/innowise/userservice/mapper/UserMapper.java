package com.innowise.userservice.mapper;

import com.innowise.userservice.model.dto.UserCreateDto;
import com.innowise.userservice.model.dto.UserUpdateDto;
import com.innowise.userservice.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.control.MappingControl;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateDto userCreateDto);
    UserCreateDto toUserRequestDto(User user);
    User toUser(UserUpdateDto userUpdateDto);
}
