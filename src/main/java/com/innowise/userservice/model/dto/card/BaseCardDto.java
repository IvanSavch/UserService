package com.innowise.userservice.model.dto.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseCardDto {

    @NotBlank(message = "Number can't be empty")
    @Size(min = 16,max = 16,message = "Size must be 16")
    private String number;

    @NotBlank(message = "Holder can't be null")
    private String holder;
    @NotNull(message = "Expiration date can't be null")
    private LocalDate expirationDate;
    private boolean active;
   
}
