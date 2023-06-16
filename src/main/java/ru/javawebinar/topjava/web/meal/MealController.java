package ru.javawebinar.topjava.web.meal;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealController {
    Meal create(Meal meal);

    void update(Meal meal, int id);

    Meal get(int id);

    void delete(int id);

    List<MealTo> getAll();

    List<MealTo> filter(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime);
}
