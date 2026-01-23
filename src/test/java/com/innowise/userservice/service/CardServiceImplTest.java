package com.innowise.userservice.service;

import com.innowise.userservice.exception.DuplicateCardNumber;
import com.innowise.userservice.exception.LimitCardException;
import com.innowise.userservice.model.dto.card.CardCreateDto;
import com.innowise.userservice.model.dto.card.CardUpdateDto;
import com.innowise.userservice.model.entity.Card;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private RedisTemplate<String, Card> cardRedisTemplate;
    @Mock
    private ValueOperations<String, Card> valueOperations;
    @InjectMocks
    private CardServiceImpl cardService;
    private static final Long CARD_ID = 1L;
    private static final Long USER_ID = 1L;


    @BeforeEach
    void setUp() {
        lenient().when(cardRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void create() {
        CardCreateDto dto = new CardCreateDto();
        dto.setUserId(USER_ID);
        dto.setNumber("1111");
        dto.setHolder("Ivan");
        dto.setExpirationDate(LocalDateTime.now());
        User user = new User();
        user.setId(USER_ID);
        when(userService.findById(USER_ID)).thenReturn(user);
        when(cardRepository.countAllByUserId(user.getId())).thenReturn(0);
        when(cardRepository.findCardNumber("1111")).thenReturn(null);
        when(cardRepository.save(any(Card.class))).thenAnswer(i -> i.getArgument(0));
        Card result = cardService.create(dto);
        assertNotNull(result);
        assertEquals("1111", result.getNumber());
        assertEquals(user, result.getUser());
    }
    @Test
    void checkCreateLimitedCardException(){
        CardCreateDto dto = new CardCreateDto();
        User user = new User();
        user.setId(USER_ID);
        dto.setUserId(USER_ID);
        when(userService.findById(USER_ID)).thenReturn(user);
        when(cardRepository.countAllByUserId(USER_ID)).thenReturn(5);
        assertThrows(LimitCardException.class, () -> cardService.create(dto));
        verify(cardRepository, never()).save(any());
    }
    @Test
    void checkCreateDuplicateCardException(){
        User user = new User();
        user.setId(USER_ID);
        Card card = new Card();
        card.setNumber("1111");
        CardCreateDto dto = new CardCreateDto();
        dto.setNumber("1111");
        dto.setUserId(USER_ID);
        when(userService.findById(USER_ID)).thenReturn(user);
        when(cardRepository.findCardNumber("1111")).thenReturn("1111");
        assertThrows(DuplicateCardNumber.class, ()-> cardService.create(dto));
    }

    @Test
    void findByIdFromCache() {
        Card card = new Card();
        card.setId(CARD_ID);
        when(valueOperations.get("card:1")).thenReturn(card);
        Card result = cardService.findById(CARD_ID);
        assertEquals(CARD_ID, result.getId());
        verify(cardRepository, never()).findById(any());
    }

    @Test
    void findByIdFromDB() {
        Card card = new Card();
        card.setId(CARD_ID);
        when(valueOperations.get("card:1")).thenReturn(null);
        when(cardRepository.findById(CARD_ID)).thenReturn(Optional.of(card));
        Card result = cardService.findById(CARD_ID);
        assertEquals(CARD_ID, result.getId());
    }
    @Test
    void findAllCardByUserId() {
        User user = new User();
        user.setId(USER_ID);
        Card card1 = new Card();
        card1.setId(CARD_ID);
        card1.setUser(user);
        Card card2 = new Card();
        card2.setId(2L);
        card2.setUser(user);
        List<Card> cards = List.of(card1, card2);
        when(cardRepository.findAllByUserId(USER_ID)).thenReturn(cards);
        List<Card> result = cardService.findAllCardByUserId(USER_ID);
        assertEquals(2, result.size());
        verify(cardRepository).findAllByUserId(USER_ID);
    }
    @Test
    void findAll(){
        Pageable pageable = PageRequest.of(0,2);
        Card card1 = new Card();
        card1.setId(CARD_ID);
        Card card2 = new Card();
        card2.setId(2L);
        List<Card> cards = List.of(card1, card2);
        Page<Card> cardPage = new PageImpl<>(cards);
        when(cardRepository.findAll(pageable)).thenReturn(cardPage);
        List<Card> result = cardService.findAllCard(pageable);
        assertEquals(2, result.size());
        verify(cardRepository).findAll(pageable);
    }

    @Test
    void updateById() {
        User user = new User();
        user.setId(USER_ID);
        Card card = new Card();
        card.setId(CARD_ID);
        card.setNumber("1111");
        CardUpdateDto cardDto = new CardUpdateDto();
        cardDto.setNumber("2222");
        cardDto.setUserId(USER_ID);
        when(cardRepository.findById(CARD_ID)).thenReturn(Optional.of(card));
        when(cardRepository.findCardNumber("2222")).thenReturn(null);
        when(userService.findById(USER_ID)).thenReturn(user);
        when(cardRepository.save(any(Card.class))).thenAnswer(i -> i.getArgument(0));
        Card updateCard = cardService.updateById(CARD_ID, cardDto);
        assertEquals(CARD_ID, updateCard.getId());
        assertEquals(user, updateCard.getUser());
        assertEquals("2222",updateCard.getNumber());
        verify(cardRedisTemplate).delete("card:1");
    }
    @Test
    void activateCardById() {
        Card card = new Card();
        card.setId(CARD_ID);
        when(cardRepository.findById(CARD_ID))
                .thenReturn(Optional.of(card));
        cardService.activateCardById(CARD_ID);
        verify(cardRepository).activateCardById(CARD_ID);
        verify(cardRedisTemplate).delete("card:1");
    }

    @Test
    void deactivateCardById() {
        Card card = new Card();
        card.setId(CARD_ID);
        when(cardRepository.findById(CARD_ID)).thenReturn(Optional.of(card));
        cardService.deactivateCardById(CARD_ID);
        verify(cardRepository).deactivateCardById(CARD_ID);
        verify(cardRedisTemplate).delete("card:1");
    }

    @Test
    void deleteUser() {
        Card card = new Card();
        card.setId(CARD_ID);
        cardService.deleteUser(card);
        verify(cardRepository).delete(card);
        verify(cardRedisTemplate).delete("card:1");

    }
}