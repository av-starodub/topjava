package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface Dao {
    Meal doGet(Integer id);

    void doCreate(HttpServletRequest request, HttpServletResponse response);

    void doUpdate(HttpServletRequest request, HttpServletResponse response);

    void doDelete(HttpServletRequest request, HttpServletResponse response);

    List<Meal> getAll();
}
