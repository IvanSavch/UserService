package com.innowise.userservice.repository;

import com.innowise.userservice.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = "UPDATE users set active = true where id=?1")
    boolean activateUserById(Long id);
    @Query(nativeQuery = true, value = "UPDATE users set active = false where id=?1")
    boolean deactivateUserById(Long id);
}
