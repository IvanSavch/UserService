package com.innowise.userservice.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateDto {
    @NotNull(message = "Id can't be null")
    @NotBlank(message = "Id can't be empty")
    private Long id;
    @NotNull(message = "Name can't be null")
    @NotBlank(message = "Name can't be empty")
    private String name;

    @NotNull(message = "Surname can't be null")
    @NotBlank(message = "Surname can't be empty")
    private String surname;

    @NotNull(message = "Birth date can't be null")
    @NotBlank(message = "Birth date can't be empty")
    private LocalDate birthDate;

    @NotNull(message = "Email date can't be null")
    @NotBlank(message = "Email date can't be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull
    @NotBlank
    private boolean active;
}
