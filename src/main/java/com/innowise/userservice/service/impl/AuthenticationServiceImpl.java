package com.innowise.userservice.service.impl;

import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.service.AuthenticationService;
import com.innowise.userservice.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;

    public AuthenticationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean adminRole(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));
    }

    @Override
    public boolean isSelf(Long userId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        User byId = userService.findById(userId);
        Long id = (Long)authentication.getPrincipal();
        return byId.getAuthId().equals(id);
    }
}
