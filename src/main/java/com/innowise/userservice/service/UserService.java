package com.innowise.userservice.service;

import com.innowise.userservice.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void create(User user);

    Optional<User> findById(Long id);

    Page<User> findAllUser(Pageable pageable);

    void updateById(User user);

    boolean activateUserById(Long id);

    boolean deactivateUserById(Long id);

    void deleteUser(User user);
}
