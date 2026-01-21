package com.innowise.userservice.service;

import com.innowise.userservice.exception.DuplicateEmailException;
import com.innowise.userservice.model.dto.user.UserCreateDto;
import com.innowise.userservice.model.dto.user.UserUpdateDto;
import com.innowise.userservice.model.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    void create(UserCreateDto userCreateDto) throws DuplicateEmailException;

    User findById(Long id);

    List<User> findAllUser(Pageable pageable);

    void updateById(Long id,UserUpdateDto userUpdateDto);

    void activateUserById(Long id);

    void deactivateUserById(Long id);

    void deleteUser(User user);
}
