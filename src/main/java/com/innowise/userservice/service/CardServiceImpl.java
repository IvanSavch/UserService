package com.innowise.userservice.service;

import com.innowise.userservice.exception.LimitCardException;
import com.innowise.userservice.mapper.CardMapper;
import com.innowise.userservice.model.dto.CardCreateDto;
import com.innowise.userservice.model.dto.CardUpdateDto;
import com.innowise.userservice.model.entity.Card;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final CardMapper cardMapper;
    private final UserService userService;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper, UserService userService) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.userService = userService;
    }


    @Override
    @Transactional
    public void create(Long userId, CardCreateDto cardCreateDto) throws LimitCardException {
        User user = userService.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (cardRepository.countAllByUserId(user.getId()) >= 5) {
            throw new LimitCardException("User can't have more than 5 cards");
        }

        Card card = cardMapper.toCard(cardCreateDto);
        card.setUser(user);
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
    public void updateById(CardUpdateDto cardUpdateDto) {
        Card card = cardMapper.toCard(cardUpdateDto);
        User user = userService.findById(cardUpdateDto.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        card.setUser(user);
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
    @Transactional
    public void deleteUser(Card card) {
        cardRepository.delete(card);
    }
}
