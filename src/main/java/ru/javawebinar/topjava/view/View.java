package ru.javawebinar.topjava.view;

import ru.javawebinar.topjava.dao.Dao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface View {
    String getUrl();

    void showNew(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    void showEdit(HttpServletRequest request, HttpServletResponse response, Dao dao) throws ServletException, IOException;

    void showDelete(HttpServletRequest request, HttpServletResponse response, Dao dao) throws IOException, ServletException;

    void showAll(HttpServletRequest request, HttpServletResponse response, Dao dao) throws ServletException, IOException;
}
