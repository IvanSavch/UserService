package com.innowise.userservice.model.dto;

import com.innowise.userservice.model.entity.User;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardUpdateDto {
    @NotNull(message = "Id can't be null")
    @NotBlank(message = "Id can't be empty")
    private Long id;
    @NotNull(message = "Number can't be null")
    @NotBlank(message = "Number can't be empty")
    @Size(max = 16)
    private String number;
    @NotNull(message = "Holder can't be null")
    @NotBlank(message = "Holder can't be empty")
    private String holder;
    @NotNull(message = "Expiration date can't be null")
    @NotBlank(message = "Expiration date can't be empty")
    private LocalDateTime expirationDate;
    @NotNull
    @NotBlank
    private boolean active;
    @NotNull(message = "User id can't be null")
    @NotBlank(message = "User id can't be empty")
    private Long userId;
}
