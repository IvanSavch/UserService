package com.innowise.userservice.repository;

import com.innowise.userservice.model.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("select c from payment_card c where c.user.id = ?1")
    List<Card> findAllByUserId(Long userId);
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM payment_card WHERE user_id = ?1 ")
    int countAllByUserId(Long id);
    Page<Card> findAll(Pageable pageable);
    @Query(nativeQuery = true, value = "UPDATE payment_card set active = true where id=?1")
    @Modifying
    void activateCardById(Long id);
    @Query(nativeQuery = true, value = "UPDATE payment_card set active = false where id=?1")
    @Modifying
    void deactivateCardById(Long id);
    @Query(nativeQuery = true,value = "SELECT number from payment_card where number = ?1")
    String findCardNumber(String number);
}
