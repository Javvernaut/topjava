package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int MEAL_ID = START_SEQ + 2;
    public static final int NOT_FOUND = 10;

    public static final List<Meal> meals = new ArrayList<Meal>() {{
        add(new Meal(MEAL_ID, LocalDateTime.parse("2020-01-30T10:00:00"), "Завтрак", 500));
        add(new Meal(MEAL_ID + 1, LocalDateTime.parse("2020-01-30T13:00:00"), "Обед", 1000));
        add(new Meal(MEAL_ID + 2, LocalDateTime.parse("2020-01-30T20:00:00"), "Ужин", 500));
        add(new Meal(MEAL_ID + 3, LocalDateTime.parse("2020-01-31T00:00:00"), "Еда на граничное значение", 100));
        add(new Meal(MEAL_ID + 4, LocalDateTime.parse("2020-01-31T10:00:00"), "Завтрак", 1000));
        add(new Meal(MEAL_ID + 5, LocalDateTime.parse("2020-01-31T13:00:00"), "Обед", 500));
        add(new Meal(MEAL_ID + 6, LocalDateTime.parse("2020-01-31T20:00:00"), "Ужин", 410));
    }};

    public static Meal getNew() {
        return new Meal(LocalDateTime.parse("2020-01-22T10:00:00"), "Новая еда", 777);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meals.get(3));
        updated.setDescription("UpdatedDescription");
        updated.setCalories(345);
        return updated;
    }


    public static void main(String[] args) {
        meals.forEach(meal -> System.out.println(meal.getId()));
    }
}
