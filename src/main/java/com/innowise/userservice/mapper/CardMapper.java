package com.innowise.userservice.mapper;

import com.innowise.userservice.model.dto.CardCreateDto;
import com.innowise.userservice.model.dto.CardUpdateDto;
import com.innowise.userservice.model.entity.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {


    Card toCard(CardCreateDto cardCreateDto);

    CardCreateDto toCardRequestDto(Card card);
    Card toCard(CardUpdateDto cardUpdateDto);
}
