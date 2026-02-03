package com.innowise.userservice.specification;

import com.innowise.userservice.model.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSpecification {

    public static Specification<User> hasName(String name){
        if (name == null){
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"),name);
    }

    public static Specification<User> hasSurname (String surname){
        if (surname == null){
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("surname"),surname);
    }
}
