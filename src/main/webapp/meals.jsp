<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>Meals list</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
          crossorigin="anonymous">
</head>
<body>
<h3><a href="${contextPath}">Home</a></h3>
<hr>
<div class="container">
    <h2 align="center">Meals</h2>
    <h3 align="center"><a href="${contextPath}/meals/new">
        <button class="btn btn-primary" type="button">Add meal</button>
    </a></h3>
    <table align="center" cellpadding="10">
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        <jsp:useBean id="meals" scope="request" type="java.util.List"/>
        <c:forEach var="meal" items="${meals}">
            <tr style="color:
            <c:choose>
            <c:when test="${meal.excess}">red</c:when>
            <c:otherwise>green</c:otherwise>
                    </c:choose>">
                <td>${meal.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a class="btn btn-primary"
                       href="${contextPath}/meals/edit?id=${meal.id}">Edit</a></td>
                <td><a class="btn btn-primary"
                       href="${contextPath}/meals/delete?id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
