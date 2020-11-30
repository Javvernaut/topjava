var ctx;

$(function () {
    ctx = {
        ajaxUrl: "profile/meals/",
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
    };
    makeEditable();
})

