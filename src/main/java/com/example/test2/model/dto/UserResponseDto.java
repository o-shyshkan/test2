package com.example.test2.model.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserResponseDto {
    @Schema(
            description = "User id in database",
            name = "id",
            type = "string",
            example = "example-user-id-1")
    private String id;
    @Schema(
            description = "Name of the user in database",
            name = "username",
            type = "string",
            example = "b.petrov")
    private String username;
    @Schema(
            description = "First name of the user",
            name = "name",
            type = "string",
            example = "Bob")
    private String name;
    @Schema(
            description = "Last name of the user",
            name = "surname",
            type = "string",
            example = "Petrov")
    private String surname;
}
