<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Users</title>
    <link type= "text/css" rel="stylesheet" href="${ pageContext.request.contextPath }/css/styles.css" />
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach items="${ meals }" var="meal">
        <tr class="${ meal.excess ? 'red' : 'green' }">
            <fmt:parseDate value="${ meal.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
            <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${ parsedDateTime }" /></td>
            <td>${ meal.description }</td>
            <td>${ meal.calories }</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
