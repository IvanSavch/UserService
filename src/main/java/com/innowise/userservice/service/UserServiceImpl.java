package com.innowise.userservice.service;


import com.innowise.userservice.exception.DuplicateEmailException;
import com.innowise.userservice.exception.UserNotFound;
import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.model.dto.user.UserCreateDto;
import com.innowise.userservice.model.dto.user.UserUpdateDto;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void create(UserCreateDto userCreateDto) {
        if (userRepository.findEmail(userCreateDto.getEmail()) != null) {
            throw new DuplicateEmailException();
        }
        User user = UserMapper.INSTANCE.toUser(userCreateDto);
        userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFound::new);
    }

    @Override
    public List<User> findAllUser(Pageable pageable) {
        return userRepository.findAll(pageable).getContent();
    }

    @Override
    public void updateById(Long id, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(id).orElseThrow(UserNotFound::new);

        if (!user.getEmail().equals(userUpdateDto.getEmail())) {
            if (userRepository.findEmail(userUpdateDto.getEmail()) != null) {
                throw new DuplicateEmailException();
            }
        }

         user = UserMapper.INSTANCE.toUser(userUpdateDto);
        user.setId(id);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void activateUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFound::new);
        userRepository.activateUserById(user.getId());
    }

    @Override
    @Transactional
    public void deactivateUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFound::new);
        userRepository.deactivateUserById(user.getId());
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        if (user == null) {
            throw new UserNotFound();
        }
        userRepository.delete(user);
    }
}
