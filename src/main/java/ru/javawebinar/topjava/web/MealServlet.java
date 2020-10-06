package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.dao.GenericDao;
import ru.javawebinar.topjava.dao.MealsInMemoryGenericDao;
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
    public static final GenericDao<Meal> meals_dao = new MealsInMemoryGenericDao();

    static {
        meals_dao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals_dao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals_dao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals_dao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals_dao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals_dao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals_dao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(meals_dao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
            request.setAttribute("mealsTo", mealsTo);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("add"))
            request.getRequestDispatcher("/edit_meal.jsp").forward(request, response);
        else if (action.equalsIgnoreCase("update")) {
            long id = Long.parseLong(request.getParameter("id"));
            Meal meal = meals_dao.getById(id);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/edit_meal.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("delete")) {
            long idToDelete = Long.parseLong(request.getParameter("id"));
            meals_dao.delete(idToDelete);
            response.sendRedirect(getServletContext().getContextPath() + "/meals");
        }
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
            meals_dao.add(meal);
        } else {
            meal.setId(Long.parseLong(idFromForm));
            meals_dao.update(meal);
        }
        response.sendRedirect(getServletContext().getContextPath() + "/meals");
    }
}
