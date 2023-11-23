package com.example.test2.controller;

import com.example.test2.model.User;
import com.example.test2.model.dto.UserResponseDto;
import com.example.test2.service.UserService;
import com.example.test2.mapper.impl.UserDtoResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserDtoResponseMapper userDtoResponseMapper;

    @Operation(summary = "Get all users ",
            description = "This method get all users from databases defined in configuration" +
                    " file application.yml")
    @GetMapping()
    public List<UserResponseDto> get() {
        List<User> users = userService.findAllUser();
        return users.stream()
                .map(userDtoResponseMapper::toDto)
                .toList();
    }

    @Operation(summary = "Get users by username",
            description = "This method get users by users name from databases defined in" +
                    " configuration file application.yml")
    @GetMapping("/find")
    public List<UserResponseDto> getByUserName(@Parameter(
                name = "username",
                description = "Username of user to be searched in databases.",
                example = "admin",
                required = true)
            @RequestParam String username) {
        List<User> users = userService.getByUserName(username);
        return users.stream()
                .map(userDtoResponseMapper::toDto)
                .toList();
    }
}

