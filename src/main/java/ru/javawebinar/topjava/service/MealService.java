package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final Logger log = getLogger(MealService.class);

    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        Meal result = repository.save(meal, userId);
        log.info("Created meal: {}", result);
        return result;
    }

    public Meal update(Meal meal, int userId) {
        Meal result = repository.save(meal, userId);
        checkNotFoundWithId(result, meal.getId());
        log.info("Updated meal: {}", result);
        return result;
    }

    public boolean delete(int mealId, int userId) {
        boolean result = repository.delete(mealId, userId);
        checkNotFoundWithId(result, mealId);
        log.info("Meal with id={} deleted", mealId);
        return true;
    }

    public Meal get(int mealId, int userId) {
        Meal result = repository.get(mealId, userId);
        checkNotFoundWithId(result, mealId);
        log.info("Gotten meal: {}", result);
        return result;
    }

    public List<Meal> getAll(int userId) {
        List<Meal> result = new ArrayList<>(repository.getAll(userId));
        log.info("All meals: {}", result);
        return result;
    }

    public List<Meal> getFiltered(int userId, Predicate<Meal> filter) {
        List<Meal> result = new ArrayList<>(repository.getFiltered(userId, filter));
        log.info("Filtered meals: {}", result);
        return result;
    }
}