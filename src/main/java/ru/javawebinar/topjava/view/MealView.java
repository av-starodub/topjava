package ru.javawebinar.topjava.view;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public final class MealView implements View {
    private final String url;
    private final Logger log;

    public MealView() {
        url = "/meals";
        log = getLogger(MealView.class);
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void showNew(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to new");
        doShow(request, response, "/new.jsp");
    }

    @Override
    public void showEdit(HttpServletRequest request, HttpServletResponse response, Dao mealDao) throws ServletException, IOException {
        log.debug("redirect to edit");
        doShow(request, response, mealDao, "/edit.jsp");
    }

    @Override
    public void showDelete(HttpServletRequest request, HttpServletResponse response, Dao mealDao) throws ServletException, IOException {
        log.debug("redirect to delete");
        doShow(request, response, mealDao, "/delete.jsp");
    }

    @Override
    public void showAll(HttpServletRequest request, HttpServletResponse response, Dao mealDao) throws ServletException, IOException {
        log.debug("redirect to meals");
        request.setAttribute("meals", MealsUtil.toListMealTo(mealDao.getAll(), MealsUtil.CALORIES_PER_DAY));
        doShow(request, response, "/meals.jsp");
    }

    private void doShow(HttpServletRequest request, HttpServletResponse response, Dao mealDao, String urlJsp) throws ServletException, IOException {
        if (Objects.nonNull(mealDao)) {
            Integer id = ServletUtil.getId(request);
            Meal meal = mealDao.doGet(id);
            if (Objects.isNull(meal)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            request.setAttribute("meal", meal);
        }
        request.getRequestDispatcher(urlJsp).forward(request, response);
    }

    private void doShow(HttpServletRequest request, HttpServletResponse response, String urlJsp) throws ServletException, IOException {
        doShow(request, response, null, urlJsp);
    }
}
