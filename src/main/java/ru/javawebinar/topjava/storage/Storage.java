package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {
    void add(Meal meal);

    Meal get(Integer id);

    void update(Meal meal, Integer uniqueID);

    void delete(Integer uniqueID);

    List<Meal> getAll();
}
