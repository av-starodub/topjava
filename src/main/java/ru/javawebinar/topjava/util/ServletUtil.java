package ru.javawebinar.topjava.util;

import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.http.HttpServletRequest;

public final class ServletUtil {
    private ServletUtil() {
    }

    public static int getId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }

    public static String getAction(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            return "list";
        }
        String[] pathParts = pathInfo.split("/");
        return ArrayUtils.get(pathParts, 1, "");
    }
}
