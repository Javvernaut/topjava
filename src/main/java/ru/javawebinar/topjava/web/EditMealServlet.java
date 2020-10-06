package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.Main;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class EditMealServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action != null && action.equalsIgnoreCase("delete")) {
            long idToDelete = Long.parseLong(request.getParameter("id"));
            Main.MEALS_DAO.deleteRecord(idToDelete);
            response.sendRedirect("/topjava/meals");
            return;
        }

        String idFromForm = request.getParameter("id");
        String dateTimeFromForm = request.getParameter("dateTime");
        String descriptionFromForm = request.getParameter("description");
        String caloriesFromForm = request.getParameter("calories");
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTimeFromForm, Main.DATE_TIME_FORMATTER);
        Meal meal = new Meal(parsedDateTime, descriptionFromForm, Integer.parseInt(caloriesFromForm));
        if (idFromForm == null || idFromForm.isEmpty()) {
            Main.MEALS_DAO.addRecord(meal);
        }
        else {
            meal.setId(Long.parseLong(idFromForm));
            Main.MEALS_DAO.updateRecord(meal);
        }

        response.sendRedirect("/topjava/meals");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("add"))
            request.getRequestDispatcher("/edit_meal.jsp").forward(request, response);
        else if (action.equalsIgnoreCase("update")) {
            long id = Long.parseLong(request.getParameter("id"));
            Meal meal = Main.MEALS_DAO.getRecordById(id);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/edit_meal.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("delete")) {
            doPost(request, response);
        }
    }
}
