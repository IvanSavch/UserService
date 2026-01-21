package com.innowise.userservice.repository;

import com.innowise.userservice.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE users set active = true where id=?1")
    void activateUserById(Long id);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE users set active = false where id=?1")
    void deactivateUserById(Long id);

    @Query(nativeQuery = true, value = "SELECT email FROM users WHERE email = ?1 ")
    String findEmail(String email);
    User findByEmail(String email);
}

