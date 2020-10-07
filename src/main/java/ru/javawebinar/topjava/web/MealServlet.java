package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.dao.GenericDao;
import ru.javawebinar.topjava.dao.MealsInMemoryDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MealServlet extends HttpServlet {
    public static final int CALORIES_PER_DAY = 2000;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    public final GenericDao<Meal> mealsDao = new MealsInMemoryDao();

    {
        mealsDao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        mealsDao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        mealsDao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        mealsDao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        mealsDao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        mealsDao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        mealsDao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null)
            switch (action) {
                case "add":
                    request.getRequestDispatcher("/edit_meal.jsp").forward(request, response);
                    return;
                case "update":
                    long id = parseId(request.getParameter("id"));
                    Meal meal = mealsDao.getById(id);
                    request.setAttribute("meal", meal);
                    request.getRequestDispatcher("/edit_meal.jsp").forward(request, response);
                    return;
                case "delete":
                    long idToDelete = parseId(request.getParameter("id"));
                    mealsDao.delete(idToDelete);
                    response.sendRedirect(getServletContext().getContextPath() + "/meals");
                    return;
            }

        List<MealTo> mealsTo = MealsUtil.filteredByStreams(mealsDao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
        request.setAttribute("mealsTo", mealsTo);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idFromForm = request.getParameter("id");
        String dateTimeFromForm = request.getParameter("dateTime");
        String descriptionFromForm = request.getParameter("description");
        String caloriesFromForm = request.getParameter("calories");
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTimeFromForm, DATE_TIME_FORMATTER);
        Meal meal = new Meal(parsedDateTime, descriptionFromForm, Integer.parseInt(caloriesFromForm));
        if (idFromForm == null || idFromForm.isEmpty()) {
            mealsDao.add(meal);
        } else {
            meal.setId(parseId(idFromForm));
            mealsDao.update(meal);
        }
        response.sendRedirect(getServletContext().getContextPath() + "/meals");
    }

    private long parseId(String id) {
        return Long.parseLong(id);
    }
}
