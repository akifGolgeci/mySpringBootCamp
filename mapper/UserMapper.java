package com.example.user2.mapper;

import com.example.user2.dto.UserRequestDto;
import com.example.user2.dto.UserResponseDto;
import com.example.user2.entity.User;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User userRequestToDto(UserRequestDto userRequestDto);
    UserResponseDto userToUserResponse(User user);
    List<UserResponseDto> userToUserResponseDto(List<User> users);
}
