package ru.javawebinar.topjava.dao;

import java.util.List;

public interface GenericDAO<T> {
    T getRecordById(long id);
    List<T> getAllRecords();
    void addRecord(T object);
    void updateRecord(T object);
    void deleteRecord(long id);
}
