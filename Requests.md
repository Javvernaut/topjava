####GetMeal
curl --location --request GET 'localhost:8080/topjava/rest/meals/100003'

####Delete meal
curl --location --request DELETE 'localhost:8080/topjava/rest/meals/100005'

####Get All meals
curl --location --request GET 'http://localhost:8080/topjava/rest/meals'

####Create meal
curl --location --request POST 'localhost:8080/topjava/rest/meals' \
--header 'Content-Type: application/json' \
--data-raw '{
    "dateTime":"2020-05-23T13:13:02",
    "description":"Awesome",
    "calories":333
}'

####Get filtered meals
curl --location --request GET 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-31&startTime=10:00:00&endTime=17:00:00'

####Update meal
curl --location --request PUT 'localhost:8080/topjava/rest/meals/100002' \
--header 'Content-Type: application/json' \
--data-raw '{
    "dateTime": "2020-05-30T20:00:00",
    "description": "Обновленная еда",
    "calories": 200
}'