package com.innowise.userservice.model.dto.card;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardUpdateDto extends BaseCardDto {
    @NotNull(message = "User id can't be null")
    private Long userId;
}
