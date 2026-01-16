package com.innowise.userservice.service;

import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(User user) {
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
    public void updateById(User user) {
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
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
