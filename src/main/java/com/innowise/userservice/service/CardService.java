package com.innowise.userservice.service;

import com.innowise.userservice.exception.LimitCardException;
import com.innowise.userservice.model.dto.card.CardCreateDto;
import com.innowise.userservice.model.dto.card.CardUpdateDto;
import com.innowise.userservice.model.entity.Card;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CardService {
    Card create(CardCreateDto cardCreateDto);
    Card findById(Long id);

    List<Card> findAllCard(Pageable pageable);

    Card updateById(Long id,CardUpdateDto cardUpdateDto);

    List<Card> findAllCardByUserId(Long userId);

    void activateCardById(Long id);

    void deactivateCardById(Long id);

    void deleteCard(Card card);
}
