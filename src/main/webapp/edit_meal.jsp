<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="${ pageContext.request.contextPath }">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<form method="POST" action="${pageContext.request.contextPath}/meals/edit" name="formAddMeal">
    <input type="hidden" name="id" value="${ meal.id }">
    <table>
        <tr>
            <td>DateTime:</td>
            <td><input type="datetime-local" name="dateTime" value="${ meal.dateTime }"></td>
        </tr>
        <tr>
            <td>Description:</td>
            <td><input type="text" name="description" size="50" value="${ meal.description}"></td>
        </tr>
        <tr>
            <td>Calories:</td>
            <td><input type="text" name="calories" size="50" value="${ meal.calories}"></td>
        </tr>
    </table>
    <input type="submit" value="Save">
    <input type="button" value="Cancel" onclick="window.location='${ pageContext.request.contextPath }/meals'">
</form>

</body>
</html>
