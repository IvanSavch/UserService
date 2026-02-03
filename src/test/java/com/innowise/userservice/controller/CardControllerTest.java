package com.innowise.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.userservice.model.dto.card.CardCreateDto;
import com.innowise.userservice.model.dto.card.CardStatusDto;
import com.innowise.userservice.model.dto.card.CardUpdateDto;
import com.innowise.userservice.model.dto.user.UserCreateDto;
import com.innowise.userservice.model.entity.Card;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.CardRepository;
import com.innowise.userservice.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {
        "jwt.access.secret=testsecret",
        "jwt.access.expiration=3600000"})
class CardControllerTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")
            .waitingFor(Wait.forListeningPort());
    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis")
            .withExposedPorts(6379)
            .waitingFor(Wait.forListeningPort());
    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        redis.start();
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }
    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", () -> postgres.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgres.getUsername());
        registry.add("spring.datasource.password", () -> postgres.getPassword());

    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private  CardRepository cardRepository;
    @Autowired
    private  UserRepository userRepository;
    private static User testUser;
    private static Card testCard;

    @BeforeAll
     void  setUp() {
        testUser = new User();
        testUser.setName("Ivan");
        testUser.setSurname("Sauchanka");
        testUser.setEmail("ivan@mail.com");
        testUser.setBirthDate(LocalDate.now());
        userRepository.save(testUser);

        testCard = new Card();
        testCard.setHolder("Ivan");
        testCard.setNumber("1234123412341234");
        testCard.setExpirationDate(LocalDate.now().plusYears(2));
        testCard.setActive(false);
        testCard.setUser(testUser);
        cardRepository.save(testCard);
    }

    @AfterAll
     void tearDown() {

        cardRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createCard() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setSurname("Sauchanka");
        userCreateDto.setBirthDate(LocalDate.now());
        userCreateDto.setName("Ivan");
        userCreateDto.setEmail("i@mail.com");

        String userResponse;
        try {
            userResponse = mockMvc.perform(post("/users")
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
        cardDto.setNumber("9234123412341234");
        cardDto.setHolder("Ivan");
        cardDto.setExpirationDate(LocalDate.now());
        cardDto.setActive(true);

        try {
            mockMvc.perform(post("/cards")
                            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(cardDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.number").value("9234123412341234"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateCard() throws Exception {
        CardUpdateDto cardUpdateDto = new CardUpdateDto();
        cardUpdateDto.setHolder("new holder");
        cardUpdateDto.setNumber("1234123412341234");
        cardUpdateDto.setExpirationDate(LocalDate.now());
        cardUpdateDto.setActive(true);
        cardUpdateDto.setUserId(testUser.getId());

        mockMvc.perform(put("/cards/{id}", testCard.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(cardUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCard.getId()))
                .andExpect(jsonPath("$.holder").value("new holder"));
    }

    @Test
    void findById() throws Exception {

        mockMvc.perform(get("/cards/{id}", testCard.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCard.getId()));
    }

    @Test
    void findAll() throws Exception {
        Card card1 = new Card();
        card1.setUser(testUser);
        card1.setHolder("Ivan");
        card1.setNumber("0234123412341234");
        card1.setExpirationDate(LocalDate.now());
        card1.setActive(true);

        cardRepository.save(card1);

        mockMvc.perform(get("/cards").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(testCard.getId()))
                .andExpect(jsonPath("$[0].userId").value(testUser.getId()));
    }

    @Test
    void findAllByUserId() throws Exception {
        mockMvc.perform(get("/cards/users/{id}", testCard.getUser().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void setStatus() throws Exception {
        Long cardId = testCard.getId();
        CardStatusDto statusDto = new CardStatusDto();
        statusDto.setActive(true);
        mockMvc.perform(patch("/cards/{id}", cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }


    @Test
    void deleteCard() throws Exception {

        mockMvc.perform(delete("/cards/{id}", testCard.getId())).andExpect(status().isNoContent());
    }
}