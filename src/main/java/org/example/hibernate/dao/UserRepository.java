package org.example.hibernate.dao;

import jakarta.persistence.EntityManager;
import org.example.hibernate.entity.User;

public class UserRepository extends BaseRepository<Long, User> {

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }
}
