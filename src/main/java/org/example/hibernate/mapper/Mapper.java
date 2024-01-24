package org.example.hibernate.mapper;

public interface Mapper<F, T> {
    T mapFrom(F from);
}
