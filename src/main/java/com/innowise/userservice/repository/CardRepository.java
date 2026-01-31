package com.innowise.userservice.repository;

import com.innowise.userservice.model.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {
    @Query("select c from Card c where c.user.id = ?1")
    List<Card> findAllByUserId(Long userId);
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM payment_card WHERE user_id = ?1 ")
    int countAllByUserId(Long id);

    @Query(nativeQuery = true,value = "SELECT number from payment_card where number = ?1")
    String findCardNumber(String number);
}
