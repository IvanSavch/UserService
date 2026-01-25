package com.innowise.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.userservice.model.dto.card.CardCreateDto;
import com.innowise.userservice.model.dto.card.CardUpdateDto;
import com.innowise.userservice.model.dto.user.UserCreateDto;
import com.innowise.userservice.model.entity.Card;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.CardRepository;
import com.innowise.userservice.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CardControllerTest extends AbstractTestController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void createCard() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setSurname("Sauchanka");
        userCreateDto.setBirthDate(LocalDate.now());
        userCreateDto.setName("Ivan");
        userCreateDto.setEmail("ivan@mail.com");

        String userResponse;
        try {
            userResponse = mockMvc.perform(post("/user/")
                            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userCreateDto)))
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Long userId = JsonPath.parse(userResponse).read("$.id", Long.class);

        CardCreateDto cardDto = new CardCreateDto();
        cardDto.setUserId(userId);
        cardDto.setNumber("1234123412341234");
        cardDto.setHolder("Ivan");
        cardDto.setExpirationDate(LocalDateTime.now().plusYears(2));
        cardDto.setActive(true);

        try {
            mockMvc.perform(post("/card/")
                            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(cardDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.number").value("1234123412341234"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateCard() throws Exception {
        User user = new User();
        user.setSurname("Sauchanka");
        user.setBirthDate(LocalDate.now());
        user.setName("Ivan");
        user.setEmail("ivan@mail.com");

        User u = userRepository.save(user);

        Card card = new Card();
        card.setUser(u);
        card.setHolder("Ivan");
        card.setNumber("1234123412341234");
        card.setExpirationDate(LocalDateTime.now());
        card.setActive(true);

        Card c = cardRepository.save(card);

        CardUpdateDto cardUpdateDto = new CardUpdateDto();
        cardUpdateDto.setHolder("new holder");
        cardUpdateDto.setNumber("1234123412341234");
        cardUpdateDto.setExpirationDate(LocalDateTime.now());
        cardUpdateDto.setActive(true);
        cardUpdateDto.setUserId(u.getId());

        mockMvc.perform(put("/card/{id}", c.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(cardUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(c.getId()))
                .andExpect(jsonPath("$.holder").value("new holder"));
    }

    @Test
    void findById() throws Exception {
        User user = new User();
        user.setSurname("Sauchanka");
        user.setBirthDate(LocalDate.now());
        user.setName("Ivan");
        user.setEmail("ivan@mail.com");
        User u = userRepository.save(user);

        Card card = new Card();
        card.setUser(u);
        card.setHolder("Ivan");
        card.setNumber("1234123412341234");
        card.setExpirationDate(LocalDateTime.now());
        card.setActive(true);
        Card c = cardRepository.save(card);

        mockMvc.perform(get("/card/{id}", c.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(c.getId()));
    }

    @Test
    void findAll() throws Exception {
        User user = new User();
        user.setSurname("Sauchanka");
        user.setBirthDate(LocalDate.now());
        user.setName("Ivan");
        user.setEmail("ivan@mail.com");

        User u = userRepository.save(user);

        Card card = new Card();
        card.setUser(u);
        card.setHolder("Ivan");
        card.setNumber("1234123412341234");
        card.setExpirationDate(LocalDateTime.now());
        card.setActive(true);

        Card card1 = new Card();
        card1.setUser(u);
        card1.setHolder("Ivan");
        card1.setNumber("1234123412341234");
        card1.setExpirationDate(LocalDateTime.now());
        card1.setActive(true);

        cardRepository.save(card);
        cardRepository.save(card1);

        mockMvc.perform(get("/card/").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(card.getId()))
                .andExpect(jsonPath("$[0].userId").value(user.getId()));
    }

    @Test
    void findAllByUserId() throws Exception {
        User user = new User();
        user.setSurname("Sauchanka");
        user.setBirthDate(LocalDate.now());
        user.setName("Ivan");
        user.setEmail("ivan@mail.com");

        User u = userRepository.save(user);

        Card card = new Card();
        card.setUser(u);
        card.setHolder("Ivan");
        card.setNumber("1234123412341234");
        card.setExpirationDate(LocalDateTime.now());
        card.setActive(false);

        Card c = cardRepository.save(card);

        mockMvc.perform(get("/card/user/{id}", c.getUser().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void activateCard() throws Exception {
        User user = new User();
        user.setSurname("Sauchanka");
        user.setBirthDate(LocalDate.now());
        user.setName("Ivan");
        user.setEmail("ivan@mail.com");

        User u = userRepository.save(user);

        Card card = new Card();
        card.setUser(u);
        card.setHolder("Ivan");
        card.setNumber("1234123412341234");
        card.setExpirationDate(LocalDateTime.now());
        card.setActive(false);

        Card c = cardRepository.save(card);

        mockMvc.perform(put("/card/activate/{id}", c.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void deactivateUser() throws Exception {
        User user = new User();
        user.setSurname("Sauchanka");
        user.setBirthDate(LocalDate.now());
        user.setName("Ivan");
        user.setEmail("ivan@mail.com");

        User u = userRepository.save(user);

        Card card = new Card();
        card.setUser(u);
        card.setHolder("Ivan");
        card.setNumber("1234123412341234");
        card.setExpirationDate(LocalDateTime.now());
        card.setActive(true);

        Card c = cardRepository.save(card);

        mockMvc.perform(put("/card/deactivate/{id}",c.getId() ))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCard() throws Exception {
        User user = new User();
        user.setSurname("Sauchanka");
        user.setBirthDate(LocalDate.now());
        user.setName("Ivan");
        user.setEmail("ivan@mail.com");

        User u = userRepository.save(user);

        Card card = new Card();
        card.setUser(u);
        card.setHolder("Ivan");
        card.setNumber("1234123412341234");
        card.setExpirationDate(LocalDateTime.now());
        card.setActive(true);

        Card c = cardRepository.save(card);

        mockMvc.perform(delete("/card/{id}", c.getId())).andExpect(status().isOk());
    }
}