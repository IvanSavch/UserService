package com.innowise.userservice.controller;

import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.model.dto.user.UserCreateDto;
import com.innowise.userservice.model.dto.user.UserResponseDto;
import com.innowise.userservice.model.dto.user.UserStatusDto;
import com.innowise.userservice.model.dto.user.UserUpdateDto;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping()
    @PreAuthorize("@authenticationServiceImpl.adminRole(authentication)")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        User user = userService.create(userCreateDto);
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authenticationServiceImpl.adminRole(authentication)")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        User user = userService.updateById(id, userUpdateDto);
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);
        return ResponseEntity.ok().body(userResponseDto);
    }


    @GetMapping("/{id}")
    @PreAuthorize("@authenticationServiceImpl.adminRole(authentication) or @authenticationServiceImpl.isSelf(#id, authentication)")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping()
    @PreAuthorize("@authenticationServiceImpl.adminRole(authentication)")
    public ResponseEntity<List<UserResponseDto>> getAllUser(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false, defaultValue = "0") int page) {
        List<User> userList = userService.findAllWithFilters(PageRequest.of(page, 20), name, surname).getContent();
        if (userList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<UserResponseDto> listUserResponseDto = userMapper.toUserResponseDtoList(userList);
        return ResponseEntity.ok(listUserResponseDto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@authenticationServiceImpl.adminRole(authentication)")
    public ResponseEntity<UserResponseDto> setStatus(@PathVariable Long id, @RequestBody UserStatusDto userStatusDto) {
        User user = userService.setStatus(id, userStatusDto);
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);
        return ResponseEntity.ok().body(userResponseDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authenticationServiceImpl.adminRole(authentication)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User user = userService.findById(id);
        userService.deleteUser(user);
        return ResponseEntity.noContent().build();
    }
}
