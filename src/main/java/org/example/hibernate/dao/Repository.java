package org.example.hibernate.dao;

import org.example.hibernate.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;

public interface Repository <K extends Serializable, E extends BaseEntity<K>> {

    E save(E entity);

    void update(E entity);

    void delete(E entity);

    default Optional<E> findById(K key) {
        return findById(key, emptyMap());
    }

    Optional<E> findById(K key, Map<String, Object> properties);

    List<E> findAll();
}
