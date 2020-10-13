package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
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
    public boolean delete(int id, int userId) {
        return !isUserNotOwner(id, userId) && (mealUser.remove(id) != null && repository.remove(id) != null);
    }

    @Override
    public Meal get(int id, int userId) {
        return isUserNotOwner(id, userId) ? null : repository.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return getFiltered(userId, LocalDate.MIN, LocalDate.MAX);
    }

    @Override
    public Collection<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        return repository.values().stream()
                .filter(meal -> mealUser.get(meal.getId()) == userId)
                .filter(meal -> DateTimeUtil.isBetweenClose(meal.getDate(), startDate, endDate))
                .sorted(Comparator
                        .comparing(Meal::getDate)
                        .thenComparing(Meal::getTime).reversed()
                )
                .collect(Collectors.toList());
    }

    private boolean isUserNotOwner(int id, int userId) {
        Integer mealOwner = mealUser.get(id);
        return mealOwner == null || mealOwner != userId;
    }
}

