package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealsInMemoryGenericDao implements GenericDao<Meal> {
    private final AtomicLong idGenerator = new AtomicLong(0);
    private final Map<Long, Meal> meals_map = new ConcurrentHashMap<>();

    @Override
    public Meal getById(long id) {
        return meals_map.get(id);
    }

    @Override
    public List<Meal> getAll() {
        List<Meal> meals = new ArrayList<>(meals_map.values());
        return meals;
    }

    @Override
    public Meal add(Meal object) {
        long id = idGenerator.getAndIncrement();
        object.setId(id);
        meals_map.put(id, object);
        return object;
    }

    @Override
    public Meal update(Meal object) {
        meals_map.replace(object.getId(), object);
        return object;
    }

    @Override
    public void delete(long id) {
        meals_map.remove(id);
    }
}
