package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Autowired
    MealService mealService;

    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(ADMIN_ID);
        USER_MATCHER.assertMatch(user, admin);
        MEAL_MATCHER.assertMatch(user.getMeals(), adminMeals);
    }

    @Test
    public void getWithoutMeals() {
        User expectedUser = new User(admin);
        expectedUser.setMeals(List.of());
        mealService.delete(adminMeal1.getId(), expectedUser.getId());
        mealService.delete(adminMeal2.getId(), expectedUser.getId());
        User actualUser = service.getWithMeals(expectedUser.getId());
        USER_MATCHER.assertMatch(actualUser, expectedUser);
        Assert.assertEquals(actualUser.getMeals().size(), expectedUser.getMeals().size());
    }
}
