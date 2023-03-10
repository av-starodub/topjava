package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.memory.MealMemoryStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.ServletUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

public final class MealDao implements Dao {
    private final Storage storage;

    public MealDao() {
        storage = MealMemoryStorage.getInstance();
    }

    @Override
    public Meal doGet(Integer id) {
        return storage.get(id);
    }

    @Override
    public void doCreate(HttpServletRequest request, HttpServletResponse response) {
        Meal meal = createMeal(request);
        storage.add(meal);
    }

    @Override
    public void doUpdate(HttpServletRequest request, HttpServletResponse response) {
        Integer id = ServletUtil.getId(request);
        Meal meal = createMeal(request);
        storage.update(meal, id);
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        Integer id = ServletUtil.getId(request);
        storage.delete(id);
    }

    @Override
    public List<Meal> getAll() {
        return storage.getAll();
    }

    private Meal createMeal(HttpServletRequest request) {
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("date"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        return new Meal(dateTime, description, calories);
    }
}

