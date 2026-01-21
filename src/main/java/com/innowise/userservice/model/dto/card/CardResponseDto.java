package com.innowise.userservice.model.dto.card;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardResponseDto extends BaseCardDto {
    private Long id;
    private Long userId;
}
