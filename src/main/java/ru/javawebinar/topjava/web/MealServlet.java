package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.memory.MealMemoryStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private MealMemoryStorage storage;
    private Logger log;

    @Override
    public void init() {
        log = getLogger(MealServlet.class);
        this.storage = new MealMemoryStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = getAction(request);
        log.debug(action);
        switch (action) {
            case "list": {
                log.debug("redirect to meals");
                request.setAttribute("meals", MealsUtil.toListMealTo(storage.getAll(), MealsUtil.CALORIES_PER_DAY));
                doShow(request, response, "/meals.jsp");
                break;
            }
            case "new": {
                log.debug("redirect to new");
                doShow(request, response, "/new.jsp");
                break;
            }
            case "edit": {
                log.debug("redirect to edit");
                doShow(request, response, "/edit.jsp");
                break;
            }
            case "delete": {
                log.debug("redirect to delete");
                doShow(request, response, "/delete.jsp");
                break;
            }
            default: {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = getAction(request);
        log.debug(action);
        switch (action) {
            case "new": {
                storage.add(createMeal(request));
                break;
            }
            case "edit": {
                storage.update(createMeal(request));
                break;
            }
            case "delete": {
                storage.delete(getId(request));
                break;
            }
            default: {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        String path = request.getContextPath();
        response.sendRedirect(path + "/meals");
    }

    private static int getId(HttpServletRequest request) {
        String id = request.getParameter("id");
        if (Objects.nonNull(id)) {
            return Integer.parseInt(request.getParameter("id"));
        }
        return -1;
    }

    private String getAction(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        log.debug("pathInfo: " + pathInfo);
        if (pathInfo == null) {
            return "list";
        }
        String[] pathParts = pathInfo.split("/");
        return pathParts.length > 0 ? pathParts[1] : "";
    }

    private Meal createMeal(HttpServletRequest request) {
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("date"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(dateTime, description, calories);
        int id = getId(request);
        if (id != -1) {
            meal.setId(getId(request));
        }
        return meal;
    }

    private void doShow(HttpServletRequest request, HttpServletResponse response, String urlJsp) throws ServletException, IOException {
        int id = getId(request);
        if (id != -1) {
            Meal meal = storage.get(id);
            if (Objects.isNull(meal)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            request.setAttribute("meal", meal);
        }
        request.getRequestDispatcher(urlJsp).forward(request, response);
    }
}
