package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealServiceTest;

import static ru.javawebinar.topjava.MealTestData.getMealWithUser;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {

    @Test
    public void getWithUser() {
        Meal meal = service.getWithUser(getMealWithUser().getId(), getMealWithUser().getUser().getId());
        USER_MATCHER.assertMatch(getMealWithUser().getUser(), meal.getUser());
    }
}
