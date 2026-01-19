package com.innowise.userservice.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseUserDto {
    @NotNull(message = "Name can't be null")
    @NotBlank(message = "Name can't be empty")
    private String name;
    @NotNull(message = "Surname can't be null")
    @NotBlank(message = "Surname can't be empty")
    private String surname;
    @NotNull(message = "Birth date can't be null")
    private LocalDate birthDate;
    @NotNull(message = "Email date can't be null")
    @NotBlank(message = "Email date can't be empty")
    @Email(message = "Email not valid")
    private String email;
}
