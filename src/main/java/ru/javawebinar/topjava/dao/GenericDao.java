package ru.javawebinar.topjava.dao;

import java.util.List;

public interface GenericDao<T> {
    T getById(long id);

    List<T> getAll();

    T add(T object);

    T update(T object);

    void delete(long id);
}
