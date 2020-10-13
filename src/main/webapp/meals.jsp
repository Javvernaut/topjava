<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }

        .filter {
            position: relative;
        }

        .row {
            display: flex;
            padding-top: 10px;
        }

        .block {
            float: left;
            padding-left: 10px;
            padding-right: 10px;
        }

        input, label {
            display: block;
        }

        label {
            text-align: center;
            padding-bottom: 5px;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <div class="filter">
        <form method="get" action="meals" name="filter">
            <input type="hidden" name="action" value="filter">
            <div class="row">
                <div class="block">
                    <label for="startDate">От даты<br>(включая)</label>
                    <input type="date" name="startDate" id="startDate" value="${ param.startDate }">
                </div>
                <div class="block">
                    <label for="endDate">До даты<br>(включая)</label>
                    <input type="date" name="endDate" id="endDate" value="${ param.endDate }">
                </div>
                <div class="block">
                    <label for="startTime">От времени<br>(включая)</label>
                    <input type="time" name="startTime" id="startTime" value="${ param.startTime }">
                </div>
                <div class="block">
                    <label for="endTime">До времени<br>(исключая)</label>
                    <input type="time" name="endTime" id="endTime" value="${ param.endTime }">
                </div>
            </div>
            <div class="row">
                <div class="block">
                    <button type="submit">Фильтровать</button>
                </div>
                <div class="block">
                    <button type="reset" onclick="window.location='meals'">Отменить</button>
                </div>
            </div>
        </form>
    </div>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>