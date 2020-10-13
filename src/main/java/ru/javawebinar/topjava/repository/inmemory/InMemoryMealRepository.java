package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> mealUser = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        save(new Meal(LocalDateTime.of(2020, Month.FEBRUARY, 23, 10, 0), "Завтрак", 500), 2);
        save(new Meal(LocalDateTime.of(2020, Month.FEBRUARY, 25, 13, 33), "Обед", 2000), 2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealUser.put(meal.getId(), userId);
            repository.put(meal.getId(), meal);
            return meal;
        }

        return isUserNotOwner(meal.getId(), userId) ? null : repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int mealId, int userId) {
        return !isUserNotOwner(mealId, userId) && (mealUser.remove(mealId) != null && repository.remove(mealId) != null);
    }

    @Override
    public Meal get(int mealId, int userId) {
        return isUserNotOwner(mealId, userId) ? null : repository.get(mealId);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(meal -> mealUser.get(meal.getId()) == userId)
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Collection<Meal> getFiltered(int userId, Predicate<Meal> filter) {
        return repository.values().stream()
                .filter(meal -> mealUser.get(meal.getId()) == userId)
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean isUserNotOwner(int mealId, int userId) {
        Integer mealOwner = mealUser.get(mealId);
        return mealOwner == null || mealOwner != userId;
    }
}

