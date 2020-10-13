package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public boolean delete(int id, int userId) {
        boolean result = repository.delete(id, userId);
        checkNotFoundWithId(result, id);
        log.info("Meal with id={} deleted", id);
        return true;
    }

    public Meal get(int id, int userId) {
        Meal result = repository.get(id, userId);
        checkNotFoundWithId(result, id);
        log.info("Gotten meal: {}", result);
        return result;
    }

    public List<Meal> getAll(int userId) {
        log.info("Gotten all meals");
        return new ArrayList<>(repository.getAll(userId));
    }

    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("Gotten filtered meals");
        return new ArrayList<>(repository.getFiltered(userId, startDate, endDate));
    }
}