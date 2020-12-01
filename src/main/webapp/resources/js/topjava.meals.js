var ctx;

let ajaxUrl = "profile/meals/";

$(function () {
    ctx = {
        ajaxUrl,
        datatableApi: $("#dataTable").DataTable( {
            paging: false,
            info: true,
            columns: [
                {
                    data: "dateTime",
                },
                {
                    data: "description"
                },
                {
                    data: "calories"
                },
                {
                    defaultContent: "Edit",
                    orderable: false
                },
                {
                    defaultContent: "Delete",
                    orderable: false
                }
            ],

            order: [
                [
                    0,
                    "desc"
                ]
            ]
        })
    }
    makeEditable();
})

function saveMeal() {
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        data: $('#detailsForm').serialize(),
    }).done(function () {
        $("#editRow").modal("hide");
        filter();
        successNoty("Saved")
    })
}

function filter() {
    $.ajax({
        type: "GET",
        url: ajaxUrl + "filter",
        data: $('#filterForm').serialize()
        }).done(function (data) {
            ctx.datatableApi.clear().rows.add(data).draw()
    });
}

function resetFilter() {
    $('#filterForm')[0].reset();
    updateTable();
}

