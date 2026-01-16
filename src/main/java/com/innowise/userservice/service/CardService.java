package com.innowise.userservice.service;

import com.innowise.userservice.exception.LimitCardException;
import com.innowise.userservice.model.entity.Card;
import com.innowise.userservice.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CardService {
    void create(Card card) throws LimitCardException;

    Optional<Card> findById(Long id);

    Page<Card> findAllCard(Pageable pageable);

    void updateById(Card card);

    List<Card> findAllCardByUserId(Long userId);

    boolean activateCard(Long id);

    boolean deactivateCardById(Long id);

    void deleteUser(Card card);
}
