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
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;

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
        assertMatch(service.get(meal_1.getId(), USER_ID), meal_1);
    }

    @Test
    public void getAnothers() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(meal_2.getId(), ADMIN_ID));
    }

    @Test
    public void duplicateDateTimeCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(meal_4.getDateTime(), "Duplicate", 333), USER_ID));
    }

    @Test
    public void delete() throws Exception {
        service.delete(meal_2.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(meal_2.getId(), USER_ID));
    }

    @Test
    public void deleteAnothers() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(meal_5.getId(), ADMIN_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void getBetweenInclusive() throws Exception {
        LocalDate startDate = LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0, 0).toLocalDate();
        LocalDate endDate = LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0, 0).toLocalDate();
        List<Meal> actual = service.getBetweenInclusive(startDate, endDate, USER_ID);
        List<Meal> expected = Arrays.asList(meal_6, meal_5, meal_4, meal_3);
        assertMatch(actual, expected);
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> actual = service.getAll(USER_ID);
        List<Meal> expected = Arrays.asList(meal_6, meal_5, meal_4, meal_3, meal_2, meal_1, meal_0);
        assertMatch(actual, expected);
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), updated);
    }

    @Test
    public void updateAnothers() throws Exception {
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