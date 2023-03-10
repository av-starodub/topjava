<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Delete meal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
          crossorigin="anonymous">
</head>
<h3><a href="${pageContext.request.contextPath}">Home</a></h3>
<hr>
<body>
<div class="container">
    <h4 align="center">Удалить запись без возможности восстановления?</h4>
    <hr>
    <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal "/>
    <table align="center" cellpadding="10">
        <tr>
            <td>${meal.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
    </table>
    <hr>
    <form align="center" action='${pageContext.request.contextPath}/meals/delete?id=${meal.id}' method="post">
        <button type="submit" class="btn btn-danger">Delete</button>
        <a href="${pageContext.request.contextPath}/meals">
            <button class="btn btn-primary" type="button">Cancel</button>
        </a>
    </form>
</div>
</body>
</html>
