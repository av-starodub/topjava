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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("create meal {} userId={}", meal, userId);
        ValidationUtil.checkNew(meal);
        return service.create(meal, userId);
    }

    public void update(Meal meal, int id) {
        int userId = SecurityUtil.authUserId();
        log.info("update meal {}, userId={}", meal, userId);
        ValidationUtil.assureIdConsistent(meal, id);
        service.update(meal, userId);
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get meal id={}, userId={}", id, userId);
        return service.get(userId, id);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal id={}, userId={}", id, userId);
        service.delete(userId, id);
    }

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll userId={}", userId);
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> filter(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("filter userId={}", userId);
        LocalDate startD = getDate(startDate, "start");
        LocalDate endD = getDate(endDate, "end");
        LocalTime startT = getTime(startTime, "start");
        LocalTime endT = getTime(endTime, "end");
        Collection<Meal> meals = service.filter(userId, startD, endD);
        return MealsUtil.getFilteredTos(meals, SecurityUtil.authUserCaloriesPerDay(), startT, endT);
    }

    private LocalDate getDate(LocalDate localDate, String datePrefix) {
        if (Objects.nonNull(localDate)) {
            return localDate;
        }
        if ("start".equals(datePrefix)) {
            return LocalDate.MIN;
        }
        return LocalDate.MAX;
    }

    private LocalTime getTime(LocalTime localTime, String timePrefix) {
        if (Objects.nonNull(localTime)) {
            return localTime;
        }
        if ("start".equals(timePrefix)) {
            return LocalTime.MIN;
        }
        return LocalTime.MAX;
    }
}