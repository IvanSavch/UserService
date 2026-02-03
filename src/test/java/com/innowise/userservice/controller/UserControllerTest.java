package com.innowise.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.userservice.model.dto.user.UserCreateDto;
import com.innowise.userservice.model.dto.user.UserStatusDto;
import com.innowise.userservice.model.dto.user.UserUpdateDto;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.UserRepository;
import com.innowise.userservice.service.AuthenticationServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
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
@ActiveProfiles("test")
class UserControllerTest {

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
    private UserRepository userRepository;
    @MockitoBean(name = "authenticationServiceImpl")
    private AuthenticationServiceImpl authenticationServiceImpl;
    private static User testUser;
    @BeforeAll
    void setUp() {
        testUser = new User();
        testUser.setName("Ivan");
        testUser.setSurname("Sauchanka");
        testUser.setEmail("ivan@mail.com");
        testUser.setBirthDate(LocalDate.now());
        testUser = userRepository.save(testUser);
    }

    @AfterAll
    void cleanDb() {
        userRepository.deleteAll();
    }
    @BeforeEach
    void setupAdminRole() {
        doAnswer(invocation -> true
        ).when(authenticationServiceImpl).adminRole(any());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setSurname("Sauchanka");
        userCreateDto.setBirthDate(LocalDate.now());
        userCreateDto.setName("Ivan");
        userCreateDto.setEmail("iv@mail.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("iv@mail.com"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setSurname("Sauchanka");
        userUpdateDto.setBirthDate(LocalDate.now());
        userUpdateDto.setName("Ivan2");
        userUpdateDto.setEmail("ivn@mail.com");

        mockMvc.perform(put("/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.name").value("Ivan2"))
                .andExpect(jsonPath("$.email").value("ivn@mail.com"));

    }

    @Test
    @WithMockUser(value = "ivan@mail.com")
    void getUserById() throws Exception {
        when(authenticationServiceImpl.adminRole(any())).thenReturn(false);
        when(authenticationServiceImpl.isSelf(eq(testUser.getId()), any())).thenReturn(true);

        mockMvc.perform(get("/users/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUser() throws Exception {

        User user1 = new User();
        user1.setSurname("Sauchanka1");
        user1.setBirthDate(LocalDate.now());
        user1.setName("Ivan1");
        user1.setEmail("ivan1@mail.com");

        userRepository.save(user1);
        mockMvc.perform(get("/users")).andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void setStatus() throws Exception {
        UserStatusDto userStatusDto  =new UserStatusDto();
        userStatusDto.setActive(true);
        mockMvc.perform(patch("/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userStatusDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.active").value(userStatusDto.isActive()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", testUser.getId()))
                .andExpect(status().isNoContent());
    }
}