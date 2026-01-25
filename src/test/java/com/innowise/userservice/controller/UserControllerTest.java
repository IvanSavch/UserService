package com.innowise.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.userservice.model.dto.user.UserCreateDto;
import com.innowise.userservice.model.dto.user.UserUpdateDto;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
class UserControllerTest extends AbstractTestController {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDb() {
        userRepository.deleteAll();
    }

    @Test
    void createUser() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setSurname("Sauchanka");
        userCreateDto.setBirthDate(LocalDate.now());
        userCreateDto.setName("Ivan");
        userCreateDto.setEmail("ivan@mail.com");

        mockMvc.perform(post("/user/")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("ivan@mail.com"));

    }

    @Test
    void updateUser() throws Exception {
        User user = new User();
        user.setSurname("Sauchanka");
        user.setBirthDate(LocalDate.now());
        user.setName("Ivan");
        user.setEmail("ivan@mail.com");

        User save = userRepository.save(user);

        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setSurname("Sauchanka");
        userUpdateDto.setBirthDate(LocalDate.now());
        userUpdateDto.setName("Ivan2");
        userUpdateDto.setEmail("ivan@mail.com");

        mockMvc.perform(put("/user/{id}", save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(save.getId()))
                .andExpect(jsonPath("$.name").value(userUpdateDto.getName()));

    }

    @Test
    void getUserById() throws Exception {
        User user = new User();
        user.setSurname("Sauchanka");
        user.setBirthDate(LocalDate.now());
        user.setName("Ivan");
        user.setEmail("ivan@mail.com");

        User save = userRepository.save(user);

        mockMvc.perform(get("/user/{id}", save.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(save.getId()));
    }

    @Test
    void getAllUser() throws Exception {
        User user = new User();
        user.setSurname("Sauchanka");
        user.setBirthDate(LocalDate.now());
        user.setName("Ivan");
        user.setEmail("ivan@mail.com");

        User user1 = new User();
        user1.setSurname("Sauchanka1");
        user1.setBirthDate(LocalDate.now());
        user1.setName("Ivan1");
        user1.setEmail("ivan1@mail.com");

        userRepository.save(user);
        userRepository.save(user1);
        mockMvc.perform(get("/user/")).andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void activateUser() throws Exception {
        User user = new User();
        user.setSurname("Sauchanka");
        user.setBirthDate(LocalDate.now());
        user.setName("Ivan");
        user.setEmail("ivan@mail.com");

        User save = userRepository.save(user);

        mockMvc.perform(put("/user/activate/{id}", save.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void deactivateUser() throws Exception {
        User user = new User();
        user.setSurname("Sauchanka");
        user.setBirthDate(LocalDate.now());
        user.setName("Ivan");
        user.setEmail("ivan@mail.com");

        User save = userRepository.save(user);

        mockMvc.perform(put("/user/deactivate/{id}", save.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser() throws Exception {
        User user = new User();
        user.setSurname("Sauchanka");
        user.setBirthDate(LocalDate.now());
        user.setName("Ivan");
        user.setEmail("ivan@mail.com");

        User save = userRepository.save(user);

        mockMvc.perform(delete("/user/{id}", save.getId()))
                .andExpect(status().isOk());
    }
}