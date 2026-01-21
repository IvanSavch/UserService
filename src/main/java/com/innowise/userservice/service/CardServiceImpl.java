package com.innowise.userservice.service;

import com.innowise.userservice.exception.LimitCardException;
import com.innowise.userservice.exception.DuplicateCardNumber;
import com.innowise.userservice.exception.UserNotFound;
import com.innowise.userservice.mapper.CardMapper;
import com.innowise.userservice.model.dto.card.CardCreateDto;
import com.innowise.userservice.model.dto.card.CardUpdateDto;
import com.innowise.userservice.model.entity.Card;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserService userService;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, UserService userService) {
        this.cardRepository = cardRepository;
        this.userService = userService;
    }


    @Override
    @Transactional
    public void create(Long userId, CardCreateDto cardCreateDto) {
        User user = userService.findById(userId);
        if (cardRepository.countAllByUserId(user.getId()) >= 5) {
            throw new LimitCardException();
        }
        if (cardRepository.findCardNumber(cardCreateDto.getNumber()) != null) {
            throw new DuplicateCardNumber();
        }
        Card card = CardMapper.INSTANCE.toCard(cardCreateDto);
        card.setUser(user);
        cardRepository.save(card);

    }

    @Override
    public Card findById(Long id) {
        return cardRepository.findById(id).orElseThrow(UserNotFound::new);
    }

    @Override
    public List<Card> findAllCard(Pageable pageable) {
        return cardRepository.findAll(pageable).getContent();
    }
    @Override
    public void updateById(Long id, CardUpdateDto cardUpdateDto) {
        Card card = cardRepository.findById(id).orElseThrow(UserNotFound::new);
        if (!card.getNumber().equals(cardUpdateDto.getNumber())
                && cardRepository.findCardNumber(cardUpdateDto.getNumber()) != null){
                throw new DuplicateCardNumber();
            }
        User user = userService.findById(cardUpdateDto.getUserId());
        if (user == null){
            throw new UserNotFound();
        }

        card = CardMapper.INSTANCE.toCard(cardUpdateDto);
        card.setId(id);
        card.setUser(user);
        cardRepository.save(card);
    }

    @Override
    public List<Card> findAllCardByUserId(Long userId) {
        return cardRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public void activateCardById(Long id) {
        cardRepository.findById(id).orElseThrow(UserNotFound::new);
        cardRepository.activateCardById(id);
    }

    @Override
    @Transactional
    public void deactivateCardById(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(UserNotFound::new);
        cardRepository.deactivateCardById(card.getId());
    }

    @Override
    @Transactional
    public void deleteUser(Card card) {
        cardRepository.delete(card);
    }
}
