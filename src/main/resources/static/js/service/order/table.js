$(document).ready(function () {
    token = getCsrfTokenCookie()

    $('td').css({
        'vertical-align': 'middle !important'
    })
    
    dataTable = $('#product-order-dataTables').DataTable({
        ordering: false,
        responsive: true,
        processing: true,
        serverSide: true,
        ajax: {
            url: '/api/v1/order',
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
            { 
                data: 'image', 
                render : function (data, type, row) {
                    return generateImage(data)
                }
            },
            { data: 'name' },
            { data: 'code' },
            { data: 'stock' },
            { data: 'currencyPricePerUnit' },
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
})

function generateImage(byteImage) {
    if (byteImage != null)
        return '<center><img class="previewImageProductOrder" alt="Image Preview" src="data:image/png;base64,' + byteImage + '" style="marginTop: 1px; width: 40px; height: 80px; border: 1px solid #ddd; padding: 5px"></center>'
    else return '<center><img class="previewImageProductOrder" alt="Image Preview" src="" style="display: none;"></center>'
}
