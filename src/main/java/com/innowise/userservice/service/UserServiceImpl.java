package com.innowise.userservice.service;


import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.model.dto.UserCreateDto;
import com.innowise.userservice.model.dto.UserUpdateDto;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Override
    @Transactional
    public void create(UserCreateDto userCreateDto) {
        User user = userMapper.toUser(userCreateDto);
        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Page<User> findAllUser(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public void updateById(UserUpdateDto userUpdateDto) {
        User user = userMapper.toUser(userUpdateDto);
        userRepository.save(user);
    }

    @Override
    public boolean activateUserById(Long id) {
        return userRepository.activateUserById(id);
    }

    @Override
    public boolean deactivateUserById(Long id) {
        return userRepository.deactivateUserById(id);
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
