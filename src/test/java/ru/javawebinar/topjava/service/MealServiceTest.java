package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.Util;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() throws Exception {
        Meal actualMeal = service.get(100003, USER_ID);
        Meal expectedMeal = meals.stream()
                .filter(meal -> meal.getId() == 100003)
                .findFirst()
                .orElse(null);
        assertMatch(actualMeal, expectedMeal);
    }

    @Test
    public void getAnothers() {
        assertThrows(NotFoundException.class, () -> service.get(100003, ADMIN_ID));
    }

    @Test
    public void duplicateDateTimeCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(LocalDateTime.parse("2020-01-31T00:00:00"), "Duplicate", 333), USER_ID));
    }

    @Test
    public void delete() throws Exception {
        Integer id = meals.iterator().next().getId();
        service.delete(id, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(id, USER_ID));
    }

    @Test
    public void deleteAnothers() {
        assertThrows(NotFoundException.class, () -> service.delete(100003, ADMIN_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void getBetweenInclusive() throws Exception {
        LocalDate startDate = LocalDateTime.parse("2020-01-29T00:00:00").toLocalDate();
        LocalDate endDate = LocalDateTime.parse("2020-01-30T00:00:00").toLocalDate();
        List<Meal> actualMeals = service.getBetweenInclusive(startDate, endDate, USER_ID);
        List<Meal> expectedMeals = meals.stream()
                .filter(meal -> Util.isBetweenHalfOpen(
                        meal.getDateTime(),
                        DateTimeUtil.atStartOfDayOrMin(startDate),
                        DateTimeUtil.atStartOfNextDayOrMax(endDate))
                )
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
        assertMatch(actualMeals, expectedMeals);
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> actualMeals = service.getAll(USER_ID);
        List<Meal> expectedMeals = meals.stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
        assertMatch(actualMeals, expectedMeals);
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), updated);
    }

    @Test
    public void updateAnothers() {
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }
}