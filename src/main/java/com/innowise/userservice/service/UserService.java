package com.innowise.userservice.service;

import com.innowise.userservice.model.dto.user.UserCreateDto;
import com.innowise.userservice.model.dto.user.UserStatusDto;
import com.innowise.userservice.model.dto.user.UserUpdateDto;
import com.innowise.userservice.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {
    User create(UserCreateDto userCreateDto);

    User findById(Long id);

    Page<User> findAll(Pageable pageable);
    Page<User> findAllWithFilters(Pageable pageable, String name,String surname);

    User updateById(Long id, UserUpdateDto userUpdateDto);

    User setStatus(Long id, UserStatusDto userStatusDto);

    void deleteUser(User user);
}
