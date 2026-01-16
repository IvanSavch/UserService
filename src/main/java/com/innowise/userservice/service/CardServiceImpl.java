package com.innowise.userservice.service;

import com.innowise.userservice.exception.LimitCardException;
import com.innowise.userservice.model.entity.Card;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public void create(Card card) throws LimitCardException {
        if (cardRepository.countAllByUserId(card.getUser().getId()) >= 5) {
            throw new LimitCardException("User have more than 5 cards");
        }
        cardRepository.save(card);
    }

    @Override
    public Optional<Card> findById(Long id) {
        return cardRepository.findById(id);
    }

    @Override
    public Page<Card> findAllCard(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }

    @Override
    public void updateById(Card card) {
        cardRepository.save(card);
    }

    @Override
    public List<Card> findAllCardByUserId(Long userId) {
        return cardRepository.findAllByUserId(userId);
    }

    @Override
    public boolean activateCard(Long id) {
        return cardRepository.activateCardById(id);
    }

    @Override
    public boolean deactivateCardById(Long id) {
        return cardRepository.deactivateCardById(id);
    }

    @Override
    public void deleteUser(Card card) {
        cardRepository.delete(card);
    }
}
