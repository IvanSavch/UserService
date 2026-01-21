package com.innowise.userservice.controller;

import com.innowise.userservice.mapper.CardMapper;
import com.innowise.userservice.model.dto.card.CardCreateDto;
import com.innowise.userservice.model.dto.card.CardResponseDto;
import com.innowise.userservice.model.dto.card.CardUpdateDto;
import com.innowise.userservice.model.entity.Card;
import com.innowise.userservice.service.CardServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/card")
public class CardController {
    private final CardServiceImpl cardService;

    @Autowired
    public CardController(CardServiceImpl cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createCard(@Valid @RequestBody CardCreateDto createDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return checkValid(bindingResult);
        }
        cardService.create(createDto.getUserId(), createDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCard(@PathVariable Long id, @Valid @RequestBody CardUpdateDto cardUpdateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return checkValid(bindingResult);
        }
        cardService.updateById(id, cardUpdateDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Card card = cardService.findById(id);
        CardResponseDto cardResponseDto = CardMapper.INSTANCE.toCardResponseDto(card);
        cardResponseDto.setUserId(card.getUser().getId());
        return ResponseEntity.ok(cardResponseDto);
    }

    @GetMapping("/")
    public ResponseEntity<?> findAll(@RequestParam(required = false, defaultValue = "0") int page) {
        List<Card> allCard = cardService.findAllCard(PageRequest.of(page, 20));
        if (allCard.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<CardResponseDto> cardResponseDtoList = CardMapper.INSTANCE.toCardResponseDtoList(allCard);
        for (CardResponseDto cardResponseDto:cardResponseDtoList) {
            for (Card card:allCard) {
                cardResponseDto.setUserId(card.getUser().getId());
            }
        }
        return ResponseEntity.ok(cardResponseDtoList);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<?> findAllByUserId(@PathVariable Long id){
        List<Card> allCardByUserId = cardService.findAllCardByUserId(id);
        if (allCardByUserId.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<CardResponseDto> cardResponseDtoList = CardMapper.INSTANCE.toCardResponseDtoList(allCardByUserId);
        return ResponseEntity.ok(cardResponseDtoList);
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<?> activateCard(@PathVariable Long id) {
        cardService.activateCardById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        cardService.deactivateCardById(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Card user = cardService.findById(id);
        cardService.deleteUser(user);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> checkValid(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
