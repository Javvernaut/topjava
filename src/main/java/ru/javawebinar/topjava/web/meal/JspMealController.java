package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("meals")
public class JspMealController extends AbstractMealController {

    @GetMapping()
    public String getAll(HttpServletRequest request) {
        request.setAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("create")
    public String getCreate(HttpServletRequest request) {
        request.setAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        request.setAttribute("action", "create");
        return "mealForm";
    }

    @GetMapping("update/{id}")
    public String getUpdate(@PathVariable("id") int id,
                            HttpServletRequest request) {
        Meal meal = super.get(id);
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id,
                         HttpServletRequest request) {
        super.delete(id);
        return "redirect:/meals";
    }

    @GetMapping("filter")
    public String getBetween(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        request.setAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @PostMapping("create")
    public String postCreate(HttpServletRequest request) {
        Meal meal = getMeal(request);
        super.create(meal);
        return "redirect:/meals";
    }

    @PostMapping("update/{id}")
    public String postUpdate(@PathVariable("id") int id,
                             HttpServletRequest request) {
        Meal meal = getMeal(request);
        super.update(meal, id);
        return "redirect:/meals";
    }

    private Meal getMeal(HttpServletRequest request) {
        return new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }
}
