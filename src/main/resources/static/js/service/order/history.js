var dataTable

$(document).ready(function () {
    retrieveTransactionHostory()

    $('#order-history-dataTables').on('click', '.detail-item-btn', function () {
        window.location.href = "/order/detail/" + $(this).data('id')
    })

    $('#order-history-dataTables').on('click', '.edit-item-btn', function () {
        window.location.href = "/order/payment/" + $(this).data('id')
    })

    $('#order-history-dataTables').on('click', '.download-item-btn', function () {
        downloadPdf($(this).data('id'))
    })
})

async function retrieveTransactionHostory() {
    dataTable = await $('#order-history-dataTables').DataTable({
        responsive: true,
        processing: true,
        serverSide: true,
        ajax: {
            url: '/api/v1/order/history',
            data: function (d) {
                console
                return $.extend({}, d, {
                    search: d.search.value 
                });
            },
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
        },
        columns: [
            {
                data: null,
                render: function (data, type, row, meta) {
                    return meta.row + meta.settings._iDisplayStart + 1;
                },
                orderable: false
            },
            { data: 'orderNumber' },
            { data: 'paymentMethod' },
            { data: 'htmlStatus' },
            { data: 'createdDate' },
            { data: 'modifiedDate' },
            {
                data: 'actionsId', 
                render: function (data, type, row) { 
                    let actionId = data.id
                    let action = '<center>'
                    for(i = 0; i < data.actions.length; i++) {
                        let temp = data.actions[i]
                        action += `
                            <button class="` + temp.htmlClass + `" title="` + temp.title + `" data-id="${actionId}"><i class="fa fa-fw" aria-hidden="true">` + temp.icon + `</i></button>
                        `
                    }
                    action += '</center>'
                    return action
                },
                orderable: false, 
                searchable: false
            }
        ]
    })
}
