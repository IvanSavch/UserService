package com.innowise.userservice.service;


import com.innowise.userservice.exception.DuplicateEmailException;
import com.innowise.userservice.exception.UserNotFoundException;
import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.model.dto.user.UserCreateDto;
import com.innowise.userservice.model.dto.user.UserUpdateDto;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RedisTemplate<String, User> userRedisTemplate;
    private static final String CACHE_KEY_PREFIX = "user:";
    private static final long CACHE_TTL_MINUTES = 1;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RedisTemplate<String, User> userRedisTemplate) {
        this.userRepository = userRepository;
        this.userRedisTemplate = userRedisTemplate;
    }

    @Override
    public User create(UserCreateDto userCreateDto) {
        if (userRepository.findEmail(userCreateDto.getEmail()) != null) {
            throw new DuplicateEmailException();
        }
        User user = UserMapper.INSTANCE.toUser(userCreateDto);
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        String cacheKey = CACHE_KEY_PREFIX + id;
        User userFromCache = userRedisTemplate.opsForValue().get(cacheKey);
        if (userFromCache != null) {
            return userFromCache;
        }
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRedisTemplate.opsForValue().set(cacheKey,user,CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        return user;
    }

    @Override
    public List<User> findAllUser(Pageable pageable) {
        return userRepository.findAll(pageable).getContent();
    }

    @Override
    @Transactional
    public User updateById(Long id, UserUpdateDto userUpdateDto) {
        User userOnDB = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if (!userOnDB.getEmail().equals(userUpdateDto.getEmail()) && userRepository.findEmail(userUpdateDto.getEmail()) != null) {
                throw new DuplicateEmailException();
            }

        userOnDB = UserMapper.INSTANCE.toUser(userUpdateDto);
        userOnDB.setId(id);

        String cacheKey = CACHE_KEY_PREFIX + id;
        userRedisTemplate.delete(cacheKey);
        return userRepository.save(userOnDB);
    }

    @Override
    @Transactional
    public void activateUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        String cacheKey = CACHE_KEY_PREFIX + id;
        userRedisTemplate.delete(cacheKey);
        userRepository.activateUserById(user.getId());
    }

    @Override
    @Transactional
    public void deactivateUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        String cacheKey = CACHE_KEY_PREFIX + id;
        userRedisTemplate.delete(cacheKey);
        userRepository.deactivateUserById(user.getId());
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        if (user == null) {
            throw new UserNotFoundException();
        }
        String cacheKey = CACHE_KEY_PREFIX + user.getId();
        userRedisTemplate.delete(cacheKey);
        userRepository.delete(user);
    }
}
