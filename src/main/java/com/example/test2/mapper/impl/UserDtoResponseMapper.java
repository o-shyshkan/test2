package com.example.test2.mapper.impl;

import com.example.test2.mapper.DtoResponseMapper;
import com.example.test2.model.User;
import com.example.test2.model.dto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserDtoResponseMapper implements DtoResponseMapper<UserResponseDto, User> {
    @Override
    public UserResponseDto toDto(User object) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(object.getId());
        userResponseDto.setUsername(object.getUsername());
        userResponseDto.setName(object.getName());
        userResponseDto.setSurname(object.getSurname());
        return userResponseDto;
    }
}
