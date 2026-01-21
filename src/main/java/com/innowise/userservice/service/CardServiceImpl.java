package com.innowise.userservice.service;

import com.innowise.userservice.exception.CardNotFound;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserService userService;
    private final RedisTemplate<String,Card> cardRedisTemplate;
    private static final String CACHE_KEY_PREFIX = "card:";
    private static final long CACHE_TTL_MINUTES = 1;
    @Autowired
    public CardServiceImpl(CardRepository cardRepository, UserService userService,RedisTemplate<String,Card> cardRedisTemplate) {
        this.cardRepository = cardRepository;
        this.userService = userService;
        this.cardRedisTemplate = cardRedisTemplate;
    }


    @Override
    public Card create(Long userId, CardCreateDto cardCreateDto) {
        User user = userService.findById(userId);
        if (cardRepository.countAllByUserId(user.getId()) >= 5) {
            throw new LimitCardException();
        }
        if (cardRepository.findCardNumber(cardCreateDto.getNumber()) != null) {
            throw new DuplicateCardNumber();
        }
        Card card = CardMapper.INSTANCE.toCard(cardCreateDto);
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
        Card cardFromDB = cardRepository.findById(id).orElseThrow(CardNotFound::new);
        cardRedisTemplate.opsForValue().set(cacheKey,cardFromDB,CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        return cardFromDB;
    }

    @Override
    public List<Card> findAllCard(Pageable pageable) {
        return cardRepository.findAll(pageable).getContent();
    }
    @Override
    @Transactional
    public Card updateById(Long id, CardUpdateDto cardUpdateDto) {
        Card card = cardRepository.findById(id).orElseThrow(CardNotFound::new);
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
    public void activateCardById(Long id) {
        cardRepository.findById(id).orElseThrow(CardNotFound::new);
        cardRepository.activateCardById(id);
        String cacheKey = CACHE_KEY_PREFIX + id;
        cardRedisTemplate.delete(cacheKey);
    }

    @Override
    @Transactional
    public void deactivateCardById(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(CardNotFound::new);
        cardRepository.deactivateCardById(card.getId());
        String cacheKey = CACHE_KEY_PREFIX + id;
        cardRedisTemplate.delete(cacheKey);
    }

    @Override
    @Transactional
    public void deleteUser(Card card) {
        cardRepository.delete(card);
        String cacheKey = CACHE_KEY_PREFIX + card.getId();
        cardRedisTemplate.delete(cacheKey);
    }
}
