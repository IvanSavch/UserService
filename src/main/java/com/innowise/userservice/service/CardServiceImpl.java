package com.innowise.userservice.service;

import com.innowise.userservice.exception.CardNotFoundException;
import com.innowise.userservice.exception.LimitCardException;
import com.innowise.userservice.exception.DuplicateCardNumberException;
import com.innowise.userservice.exception.UserNotFoundException;
import com.innowise.userservice.mapper.CardMapper;
import com.innowise.userservice.model.dto.card.CardCreateDto;
import com.innowise.userservice.model.dto.card.CardStatusDto;
import com.innowise.userservice.model.dto.card.CardUpdateDto;
import com.innowise.userservice.model.entity.Card;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CardServiceImpl implements CardService {
    private static final String CACHE_KEY_PREFIX = "card:";
    private static final long CACHE_TTL_MINUTES = 1;
    private final CardRepository cardRepository;
    private final UserService userService;
    private final RedisTemplate<String,Card> cardRedisTemplate;
    private final CardMapper cardMapper;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, UserService userService, RedisTemplate<String,Card> cardRedisTemplate, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.userService = userService;
        this.cardRedisTemplate = cardRedisTemplate;
        this.cardMapper = cardMapper;
    }


    @Override
    @Transactional
    public Card create(CardCreateDto cardCreateDto) {
        User user = userService.findById(cardCreateDto.getUserId());
        if (cardRepository.countAllByUserId(user.getId()) >= 5) {
            throw new LimitCardException();
        }
        if (cardRepository.findCardNumber(cardCreateDto.getNumber()) != null) {
            throw new DuplicateCardNumberException();
        }
        Card card = cardMapper.toCard(cardCreateDto);
        card.setUser(user);
        return cardRepository.save(card);

    }

    @Override
    public Card findById(Long id) {
        String cacheKey = CACHE_KEY_PREFIX + id;
        Card card = cardRedisTemplate.opsForValue().get(cacheKey);
        if (card != null){
            return card;
        }
        Card cardFromDB = cardRepository.findById(id).orElseThrow(CardNotFoundException::new);
        cardRedisTemplate.opsForValue().set(cacheKey,cardFromDB,CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        return cardFromDB;
    }

    @Override
    public Page<Card> findAllCard(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }
    @Override
    @Transactional
    public Card updateById(Long id, CardUpdateDto cardUpdateDto) {
        Card card = cardRepository.findById(id).orElseThrow(CardNotFoundException::new);
        if (!card.getNumber().equals(cardUpdateDto.getNumber())
                && cardRepository.findCardNumber(cardUpdateDto.getNumber()) != null){
                throw new DuplicateCardNumberException();
            }
        User user = userService.findById(cardUpdateDto.getUserId());
        if (user == null){
            throw new UserNotFoundException();
        }
        card = cardMapper.toCard(cardUpdateDto);
        card.setId(id);
        card.setUser(user);
        String cacheKey = CACHE_KEY_PREFIX + id;
        cardRedisTemplate.delete(cacheKey);
        return cardRepository.save(card);
    }

    @Override
    public List<Card> findAllCardByUserId(Long userId) {
        return cardRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public Card setStatusById(Long id, CardStatusDto cardStatusDto) {
        Card card = cardRepository.findById(id).orElseThrow(CardNotFoundException::new);
        card.setActive(cardStatusDto.isActive());
        String cacheKey = CACHE_KEY_PREFIX + id;
        cardRedisTemplate.delete(cacheKey);
        return cardRepository.save(card);
    }


    @Override
    @Transactional
    public void deleteCard(Card card) {
        cardRepository.delete(card);
        String cacheKey = CACHE_KEY_PREFIX + card.getId();
        cardRedisTemplate.delete(cacheKey);
    }
}
