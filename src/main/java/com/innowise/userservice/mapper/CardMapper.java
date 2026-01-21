package com.innowise.userservice.mapper;

import com.innowise.userservice.model.dto.card.CardCreateDto;
import com.innowise.userservice.model.dto.card.CardResponseDto;
import com.innowise.userservice.model.dto.card.CardUpdateDto;
import com.innowise.userservice.model.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class );

    Card toCard(CardCreateDto cardCreateDto);
    @Mapping(target = "userId", source = "user.id")
    CardResponseDto toCardResponseDto(Card card);
    CardCreateDto toCardRequestDto(Card card);
    Card toCard(CardUpdateDto cardUpdateDto);
    List<CardResponseDto> toCardResponseDtoList(List<Card> cardList);
}
