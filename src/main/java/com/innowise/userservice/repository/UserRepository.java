package com.innowise.userservice.repository;

import com.innowise.userservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query(nativeQuery = true, value = "SELECT email FROM users WHERE email = ?1 ")
    String findEmail(String email);

    Optional<User> findByEmail(String email);
}

