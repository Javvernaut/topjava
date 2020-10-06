package ru.javawebinar.topjava;

import ru.javawebinar.topjava.dao.GenericDAO;
import ru.javawebinar.topjava.dao.MealsInMemoryGenericDAOImpl;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

/**
 * @see <a href="http://topjava.herokuapp.com">Demo application</a>
 * @see <a href="https://github.com/JavaOPs/topjava">Initial project</a>
 */
public class Main {
    public static final int CALORIES_PER_DAY = 2000;
    public static final GenericDAO<Meal> MEALS_DAO = new MealsInMemoryGenericDAOImpl();
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    static {
        MEALS_DAO.addRecord(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        MEALS_DAO.addRecord(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        MEALS_DAO.addRecord(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        MEALS_DAO.addRecord(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        MEALS_DAO.addRecord(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        MEALS_DAO.addRecord(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        MEALS_DAO.addRecord(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    public static void main(String[] args) {
        System.out.format("Hello TopJava Enterprise!");
    }
}
