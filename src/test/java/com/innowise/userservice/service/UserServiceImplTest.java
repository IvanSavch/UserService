package com.innowise.userservice.service;

import com.innowise.userservice.exception.DuplicateEmailException;
import com.innowise.userservice.exception.UserNotFoundException;
import com.innowise.userservice.mapper.CardMapper;
import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.model.dto.user.UserCreateDto;
import com.innowise.userservice.model.dto.user.UserStatusDto;
import com.innowise.userservice.model.dto.user.UserUpdateDto;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.UserRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RedisTemplate<String, User> userRedisTemplate;
    @Mock
    private ValueOperations<String, User> valueOperations;
    private static final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        lenient().when(userRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void create() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setBirthDate(LocalDate.now());
        userCreateDto.setName("test");
        userCreateDto.setSurname("Sauchanka");
        userCreateDto.setEmail("test@mail");
        User  cUser = new User();
        cUser.setBirthDate(LocalDate.now());
        cUser.setName("test");
        cUser.setSurname("Sauchanka");
        cUser.setEmail("test@mail");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(userMapper.toUser(userCreateDto)).thenReturn(cUser);
        User user = userService.create(userCreateDto);
        assertNotNull(user);
        assertEquals("test@mail",user.getEmail());
        assertEquals(user.getName(),userCreateDto.getName());
        assertEquals(user.getSurname(),userCreateDto.getSurname());
    }

    @Test
    void findByIdFromDB() {
        User user = new User();
        user.setId(USER_ID);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        assertNotNull(user);
        User result = userService.findById(USER_ID);
        assertEquals(user,result);
    }
    @Test
    void findByIdFromCache() {
        User user = new User();
        user.setId(USER_ID);
        when(valueOperations.get("user:1")).thenReturn(user);
        assertNotNull(user);
        User result = userService.findById(USER_ID);
        assertEquals(user,result);
    }
    @Test
    void findAllUser(){
        Pageable pageable = PageRequest.of(0,2);
        User user1 = new User();
        user1.setId(USER_ID);
        User user2 = new User();
        user2.setId(2L);
        List<User> users = List.of(user1, user2);
        Page<User> userPage = new PageImpl<>(users);
        when(userRepository.findAll(pageable)).thenReturn(userPage);
        Page<User> result = userService.findAll(pageable);
        assertEquals(2, result.getTotalElements());
        verify(userRepository).findAll(pageable);
    }
    @Test
    void checkExceptionUserNotFound(){
        assertThrows(UserNotFoundException.class,()->userService.findById(USER_ID));
    }
    @Test
    void checkExceptionDuplicatedEmail(){
        User user = new User();
        user.setEmail("test@mail");
        UserCreateDto userCreateDto = new UserCreateDto();
        user.setEmail("test@mail");
        when(userRepository.findEmail(userCreateDto.getEmail())).thenReturn(user.getEmail());
        assertThrows(DuplicateEmailException.class,() -> userService.create(userCreateDto));
    }

    @Test
    void updateById() {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail("test@mail.com");
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("Ivan@mail.com");
        User updatedUser = new User();
        updatedUser.setId(USER_ID);
        updatedUser.setEmail("Ivan@mail.com");
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findEmail("Ivan@mail.com")).thenReturn(null);
        when(userMapper.toUser(userUpdateDto)).thenReturn(updatedUser);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        User updateUser = userService.updateById(USER_ID,userUpdateDto);
        assertEquals("Ivan@mail.com",updateUser.getEmail());
        verify(userRedisTemplate).delete("user:1");

    }


    @Test
    void setStatusById() {
        User user = new User();
        user.setActive(false);
        user.setId(USER_ID);
        UserStatusDto userStatusDto = new UserStatusDto();
        userStatusDto.setActive(true);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        User u = userService.setStatus(USER_ID, userStatusDto);
        assertNotNull(u);
        assertTrue(u.isActive());
        verify(userRepository).findById(USER_ID);
        verify(userRedisTemplate).delete("user:1");
        verify(userRepository).save(any(User.class));

    }

    @Test
    void deleteUser() {
        User user = new User();
        user.setId(USER_ID);
        userService.deleteUser(user);
        verify(userRepository).delete(user);
        verify(userRedisTemplate).delete("user:1");
    }
}