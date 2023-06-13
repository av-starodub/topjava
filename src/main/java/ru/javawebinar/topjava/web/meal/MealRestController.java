package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

@Controller
public class MealRestController implements MealController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    @Override
    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("create meal {} userId={}", meal, userId);
        ValidationUtil.checkNew(meal);
        return service.create(meal, userId);
    }

    @Override
    public void update(Meal meal, int id) {
        int userId = SecurityUtil.authUserId();
        log.info("update meal {}, userId={}", meal, userId);
        ValidationUtil.assureIdConsistent(meal, id);
        service.update(meal, userId);
    }

    @Override
    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get meal id={}, userId={}", id, userId);
        return service.get(userId, id);
    }

    @Override
    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal id={}, userId={}", id, userId);
        service.delete(userId, id);
    }

    @Override
    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll userId={}", userId);
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }
}