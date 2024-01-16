package org.example.hibernate.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.example.hibernate.entity.AuditableEntity;

import java.time.Instant;

public class AuditDatesListener {
    @PrePersist
    private void prePersist(AuditableEntity<?> entity) {
        entity.setCreatedAt(Instant.now());
        entity.setCreatedBy("current user");
    }

    @PreUpdate
    private void preUpdate(AuditableEntity<?> entity) {
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy("current user");
    }
}
