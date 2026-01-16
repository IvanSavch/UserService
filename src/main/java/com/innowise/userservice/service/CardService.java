package com.innowise.userservice.service;

import com.innowise.userservice.model.entity.Card;

import java.util.List;
import java.util.Optional;

public interface CardService {
    void create(Card card);

    Optional<Card> findById(Long id);

    List<Card> findAllCard();

    void updateById(Card card);

    List<Card> findAllCardByUserId(Long userId);
}
