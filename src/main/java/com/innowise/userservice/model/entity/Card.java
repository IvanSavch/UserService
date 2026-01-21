package com.innowise.userservice.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "payment_card")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private String holder;
    private LocalDateTime expirationDate;
    private boolean active;

    @ManyToOne()
    @JsonBackReference
    private User user;

    public Card(String number, String holder, LocalDateTime expirationDate, boolean active, User user) {
        this.number = number;
        this.holder = holder;
        this.expirationDate = expirationDate;
        this.active = active;
        this.user = user;
    }
}
