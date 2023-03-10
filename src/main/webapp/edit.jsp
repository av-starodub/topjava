<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Edit meal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
          crossorigin="anonymous">
</head>
<h3><a href="${pageContext.request.contextPath}">Home</a></h3>
<hr>
<body>
<div class="container">
    <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal "/>
    <form style="width: 50%"action="${pageContext.request.contextPath}/meals/edit?id=${meal.id}" method="post">
        <h2>Edit meal</h2>
        <div class="mb-3">
            <label>Date</label>
            <input class="form-control" type="datetime-local" name="date" value="${meal.dateTime}">
            <label>Description</label>
            <input class="form-control" type="text"  name="description" value="${meal.description}">
            <label>Calories</label>
            <input class="form-control" type="number" name="calories" value="${meal.calories}">
        </div>
        <button class="btn btn-primary" type="submit">Save</button>
        <a href="${pageContext.request.contextPath}/meals" ><button class="btn btn-primary" type="button">Cancel</button></a>
    </form>
</div>
</body>
</html>
