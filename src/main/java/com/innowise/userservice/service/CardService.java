package com.innowise.userservice.service;

import com.innowise.userservice.model.dto.card.CardCreateDto;
import com.innowise.userservice.model.dto.card.CardStatusDto;
import com.innowise.userservice.model.dto.card.CardUpdateDto;
import com.innowise.userservice.model.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CardService {
    Card create(CardCreateDto cardCreateDto);
    Card findById(Long id);

    Page<Card> findAllCard(Pageable pageable);

    Card updateById(Long id,CardUpdateDto cardUpdateDto);

    List<Card> findAllCardByUserId(Long userId);

    Card setStatusById(Long id, CardStatusDto cardStatusDto);

    void deleteCard(Card card);
}
