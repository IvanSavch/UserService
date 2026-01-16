package com.innowise.userservice.service;

import com.innowise.userservice.model.entity.Card;
import com.innowise.userservice.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;
    @Override
    public void create(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Optional<Card> findById(Long id) {
        return cardRepository.findById(id);
    }

    @Override
    public List<Card> findAllCard() {
        return cardRepository.findAll();
    }

    @Override
    public void updateById(Card card) {
        cardRepository.save(card);
    }

    @Override
    public List<Card> findAllCardByUserId(Long userId) {
        return cardRepository.findAllByUserId(userId);
    }
}
