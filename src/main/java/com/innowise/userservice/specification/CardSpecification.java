package com.innowise.userservice.specification;

import com.innowise.userservice.model.entity.Card;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CardSpecification {
    public static Specification<Card> hasHolder(String holder){
        if (holder == null){
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("holder"),holder));
    }
}
