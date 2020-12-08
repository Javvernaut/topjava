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

$(function () {
    $.datetimepicker.setLocale(locale === 'ru' ? 'ru' : 'en');

    let startDate = $('#startDate');
    let endDate = $('#endDate');
    startDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        onShow: function () {
            this.setOptions({"maxDate": endDate.val() ? endDate.val() : false})
        }
    });

    endDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        onShow: function () {
            this.setOptions({"minDate": startDate.val() ? startDate.val() : false})
        }
    })

    let startTime = $('#startTime')
    let endTime = $('#endTime')
    startTime.datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function () {
            this.setOptions({"maxTime": endTime.val() ? endTime.val() : false})
        }
    })


    endTime.datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function () {
            this.setOptions({"minTime": startTime.val() ? startTime.val() : false})
        }
    })


    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i'

    })
})
