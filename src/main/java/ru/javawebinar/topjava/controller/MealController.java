package ru.javawebinar.topjava.controller;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.util.ServletUtil;
import ru.javawebinar.topjava.view.MealView;
import ru.javawebinar.topjava.view.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class MealController extends HttpServlet {
    private final Dao dao;
    private final View view;
    private final Logger log;

    public MealController() {
        super();
        dao = new MealDao();
        view = new MealView();
        log = getLogger(MealController.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = ServletUtil.getAction(request);
        log.debug(action);
        switch (action) {
            case "list": {
                view.showAll(request, response, dao);
                break;
            }
            case "new": {
                view.showNew(request, response);
                break;
            }
            case "edit": {
                view.showEdit(request, response, dao);
                break;
            }
            case "delete": {
                view.showDelete(request, response, dao);
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
        String action = ServletUtil.getAction(request);
        log.debug(action);
        switch (action) {
            case "new": {
                dao.doCreate(request, response);
                break;
            }
            case "edit": {
                dao.doUpdate(request, response);
                break;
            }
            case "delete": {
                dao.doDelete(request, response);
                break;
            }
            default: {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        String path = request.getContextPath();
        response.sendRedirect(path + view.getUrl());
    }
}
