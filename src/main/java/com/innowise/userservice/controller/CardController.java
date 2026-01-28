package com.innowise.userservice.controller;

import com.innowise.userservice.mapper.CardMapper;
import com.innowise.userservice.model.dto.card.CardCreateDto;
import com.innowise.userservice.model.dto.card.CardResponseDto;
import com.innowise.userservice.model.dto.card.CardStatusDto;
import com.innowise.userservice.model.dto.card.CardUpdateDto;
import com.innowise.userservice.model.entity.Card;
import com.innowise.userservice.service.CardService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;
    private final CardMapper cardMapper;

    public CardController(CardService cardService, CardMapper cardMapper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
    }

    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(@Valid @RequestBody CardCreateDto createDto) {
        Card card = cardService.create(createDto);
        CardResponseDto cardResponseDto = cardMapper.toCardResponseDto(card);
        return ResponseEntity.status(HttpStatus.CREATED).body(cardResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long id, @Valid @RequestBody CardUpdateDto cardUpdateDto) {
        Card card = cardService.updateById(id, cardUpdateDto);
        CardResponseDto cardResponseDto = cardMapper.toCardResponseDto(card);
        return ResponseEntity.ok().body(cardResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDto> findById(@PathVariable Long id) {
        Card card = cardService.findById(id);
        CardResponseDto cardResponseDto = cardMapper.toCardResponseDto(card);
        cardResponseDto.setUserId(card.getUser().getId());
        return ResponseEntity.ok(cardResponseDto);
    }

    @GetMapping()
    public ResponseEntity<List<CardResponseDto>> findAll(@RequestParam(required = false, defaultValue = "0") int page) {
        List<Card> allCard = cardService.findAllCard(PageRequest.of(page, 20)).getContent();
        List<CardResponseDto> cardResponseDtoList = cardMapper.toCardResponseDtoList(allCard);
        return ResponseEntity.ok(cardResponseDtoList);
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<List<CardResponseDto>> findAllByUserId(@PathVariable Long id){
        List<Card> allCardByUserId = cardService.findAllCardByUserId(id);
        List<CardResponseDto> cardResponseDtoList = cardMapper.toCardResponseDtoList(allCardByUserId);
        return ResponseEntity.ok(cardResponseDtoList);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CardResponseDto> setStatusCard(@PathVariable Long id, @RequestBody CardStatusDto cardStatusDto) {
        Card card = cardService.setStatus(id, cardStatusDto);
        CardResponseDto cardResponseDto = cardMapper.toCardResponseDto(card);
        return ResponseEntity.ok().body(cardResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        Card user = cardService.findById(id);
        cardService.deleteCard(user);
        return ResponseEntity.noContent().build();
    }

}
