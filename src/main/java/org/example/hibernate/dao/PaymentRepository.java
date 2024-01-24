package org.example.hibernate.dao;

import jakarta.persistence.EntityManager;
import org.example.hibernate.entity.Payment;

public class PaymentRepository extends BaseRepository<Long, Payment> {

    public PaymentRepository(EntityManager entityManager) {
        super(Payment.class, entityManager);
    }
}
