package com.innowise.userservice.service;

import com.innowise.userservice.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void create(User user);
    Optional<User> findById(Long id);
    List<User> findAllUser();
    void updateById(User user);

}
