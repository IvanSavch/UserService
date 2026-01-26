package com.innowise.userservice.mapper;

import com.innowise.userservice.model.dto.user.UserCreateDto;
import com.innowise.userservice.model.dto.user.UserResponseDto;
import com.innowise.userservice.model.dto.user.UserUpdateDto;
import com.innowise.userservice.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring", imports = {Arrays.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class );
    User toUser(UserCreateDto userCreateDto);
    User toUser(UserUpdateDto userUpdateDto);
    UserResponseDto toUserResponseDto(User user);
    List<UserResponseDto> toUserResponseDtoList(List<User> userList);
}
