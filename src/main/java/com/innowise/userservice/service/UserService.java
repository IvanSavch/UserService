package com.innowise.userservice.service;

import com.innowise.userservice.model.dto.user.UserCreateDto;
import com.innowise.userservice.model.dto.user.UserUpdateDto;
import com.innowise.userservice.model.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void create(UserCreateDto userCreateDto);

    Optional<User> findById(Long id);

    List<User> findAllUser(Pageable pageable);

    void updateById(Long id,UserUpdateDto userUpdateDto);

    boolean activateUserById(Long id);

    boolean deactivateUserById(Long id);

    void deleteUser(User user);
}
