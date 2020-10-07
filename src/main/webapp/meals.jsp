<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Users</title>
    <base href="${ pageContext.request.contextPath }/">
    <link type= "text/css" rel="stylesheet" href="css/styles.css" />
</head>
<body>
<h3><a href>Home</a></h3>
<hr>
<h2>Meals</h2>
<a href="meals?action=add">Add Meal</a>
<br>
<br>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${ mealsTo }" var="mealTo">
        <tr class="${ mealTo.excess ? 'red' : 'green' }">
            <fmt:parseDate value="${ mealTo.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
            <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${ parsedDateTime }" /></td>
            <td>${ mealTo.description }</td>
            <td>${ mealTo.calories }</td>
            <td><a href="meals?action=update&id=${ mealTo.id }">Update</a></td>
            <td><a href="meals?action=delete&id=${ mealTo.id }">Delete</a></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
