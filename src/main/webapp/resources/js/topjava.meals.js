var ctx, mealAjaxUrl = "profile/meals/";

$.ajaxSetup({
    converters: {
        "text json": function (data) {
            let inputJson = JSON.parse(data);
            $(inputJson).each(function () {
                let dateTimeSplit = this.dateTime.split('T');
                this.dateTime = dateTimeSplit[0] + " " + dateTimeSplit[1].substring(0, 5)
            })
            return inputJson;
        }
    }
})

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: "profile/meals/filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get("profile/meals/", updateTableByData);
}

$(function () {
    ctx = {
        ajaxUrl: mealAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            ajax: {
                url: mealAjaxUrl,
                dataSrc: ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data) {
                $(row).attr("data-mealExcess", data.excess)
            }
        }),
        updateTable: updateFilteredTable
    };
    makeEditable();
});

$.datetimepicker.setLocale(locale === 'ru' ? 'ru' : 'en');

$(function () {
    $('#startDate').datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
})

$(function () {
    $('#endDate').datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    })
})

$(function () {
    $('#startTime').datetimepicker({
        datepicker:false,
        format: 'H:i'
    })
})

$(function () {
    $('#endTime').datetimepicker({
        datepicker:false,
        format: 'H:i'
    })
})

$(function () {
    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i'
    })
})