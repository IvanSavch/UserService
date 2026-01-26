package com.innowise.userservice.model.dto.card;


import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CardCreateDto extends BaseCardDto {
    @NotNull(message = "User id can't be null")
    private Long userId;


}
