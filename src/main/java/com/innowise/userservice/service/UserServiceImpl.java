package com.innowise.userservice.service;


import com.innowise.userservice.exception.DuplicateEmailException;
import com.innowise.userservice.exception.UserNotFoundException;
import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.model.dto.user.UserCreateDto;
import com.innowise.userservice.model.dto.user.UserStatusDto;
import com.innowise.userservice.model.dto.user.UserUpdateDto;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.UserRepository;
import com.innowise.userservice.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl implements UserService {
    private static final String CACHE_KEY_PREFIX = "user:";
    private static final long CACHE_TTL_MINUTES = 1;
    private final UserRepository userRepository;
    private final RedisTemplate<String, User> userRedisTemplate;
    private final UserMapper userMapper;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RedisTemplate<String, User> userRedisTemplate, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userRedisTemplate = userRedisTemplate;
        this.userMapper = userMapper;
    }

    @Override
    public Page<User> findAllWithFilters(Pageable pageable, String name,String surname) {
        Specification<User> userSpecification = Specification.allOf(UserSpecification.hasName(name))
                .and(UserSpecification.hasSurname(surname));
        return userRepository.findAll(userSpecification,pageable);
    }

    @Override
    public User create(UserCreateDto userCreateDto) {
        if (userRepository.findEmail(userCreateDto.getEmail()) != null) {
            throw new DuplicateEmailException();
        }

        User user = userMapper.toUser(userCreateDto);
        User save = userRepository.save(user);
        userRedisTemplate.opsForValue().set(CACHE_KEY_PREFIX + save.getId(),save,CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        return save;
    }

    @Override
    @Transactional
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
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public User updateById(Long id, UserUpdateDto userUpdateDto) {
        User userOnDB = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if (!userOnDB.getEmail().equals(userUpdateDto.getEmail()) && userRepository.findEmail(userUpdateDto.getEmail()) != null) {
                throw new DuplicateEmailException();
            }

        userOnDB = userMapper.toUser(userUpdateDto);
        userOnDB.setId(id);

        String cacheKey = CACHE_KEY_PREFIX + id;
        userRedisTemplate.delete(cacheKey);
        return userRepository.save(userOnDB);
    }

    @Override
    @Transactional
    public User setStatus(Long id, UserStatusDto userStatusDto) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setActive(userStatusDto.isActive());

        String cacheKey = CACHE_KEY_PREFIX + id;
        userRedisTemplate.delete(cacheKey);

        return userRepository.save(user);
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
