package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealsInMemoryGenericDAOImpl implements GenericDAO<Meal> {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private static final Map<Long, Meal> MEALS_MAP = new ConcurrentHashMap<>();

    @Override
    public Meal getRecordById(long id) {
        return MEALS_MAP.get(id);
    }

    @Override
    public List<Meal> getAllRecords() {
        List<Meal> meals = new ArrayList<>(MEALS_MAP.values());
        meals.sort(Comparator.comparing(Meal::getDateTime));
        return meals;
    }

    @Override
    public void addRecord(Meal object) {
        long id = ID_GENERATOR.getAndIncrement();
        object.setId(id);
        MEALS_MAP.put(id, object);
    }

    @Override
    public void updateRecord(Meal object) {
        Meal prevObject = getRecordById(object.getId());
        if (prevObject != null)
            MEALS_MAP.put(object.getId(), object);
    }

    @Override
    public void deleteRecord(long id) {
        MEALS_MAP.remove(id);
    }
}
