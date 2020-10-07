package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealsInMemoryDao implements GenericDao<Meal> {
    private final AtomicLong idGenerator = new AtomicLong(0);
    private final Map<Long, Meal> mealsMap = new ConcurrentHashMap<>();

    @Override
    public Meal getById(long id) {
        return mealsMap.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealsMap.values());
    }

    @Override
    public Meal add(Meal object) {
        long id = idGenerator.getAndIncrement();
        object.setId(id);
        mealsMap.put(id, object);
        return object;
    }

    @Override
    public Meal update(Meal object) {
        return mealsMap.computeIfPresent(object.getId(), (key, value) -> object);
    }

    @Override
    public void delete(long id) {
        mealsMap.remove(id);
    }
}
