package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private static final Logger log = getLogger(MealRestController.class);

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        log.info("Create {}", meal);
        checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("Update {}", meal);
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("Delete id={}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        log.info("Get id={}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public List<MealTo> getAll() {
        log.info("Get all");
        List<Meal> meals = service.getAll(SecurityUtil.authUserId());
        return MealsUtil.getTos(meals, SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("Get filtered");
        List<Meal> filteredByDate = service.getFiltered(
                SecurityUtil.authUserId(),
                startDate == null ? LocalDate.MIN : startDate,
                endDate == null ? LocalDate.MAX : endDate);
        return MealsUtil.getFilteredTos(
                filteredByDate,
                SecurityUtil.authUserCaloriesPerDay(),
                startTime == null ? LocalTime.MIN : startTime,
                endTime == null ? LocalTime.MAX : endTime);
    }
}