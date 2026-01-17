package com.innowise.userservice.service;

import com.innowise.userservice.exception.LimitCardException;
import com.innowise.userservice.model.dto.CardCreateDto;
import com.innowise.userservice.model.dto.CardUpdateDto;
import com.innowise.userservice.model.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CardService {
    void create(Long userId, CardCreateDto cardCreateDto) throws LimitCardException;

    Optional<Card> findById(Long id);

    Page<Card> findAllCard(Pageable pageable);

    void updateById(CardUpdateDto cardUpdateDto);

    List<Card> findAllCardByUserId(Long userId);

    boolean activateCard(Long id);

    boolean deactivateCardById(Long id);

    void deleteUser(Card card);
}
