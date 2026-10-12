var token
var dataTable

$(document).ready(function () {
    token = getCsrfTokenCookie()
    
    dataTable = $('#product-dataTables').DataTable({
        responsive: true,
        processing: true,
        serverSide: true,
        ajax: {
            url: '/api/v1/product',
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
            { data: 'name' },
            { data: 'categoryDto.name' },
            { data: 'code' },
            { data: 'stock' },
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

    $('#add-product-btn').click(function () {
        window.location.href = "/product/add"
    })

    $('#product-dataTables').on('click', '.detail-item-btn', function () {
        window.location.href = "/product/detail/" + $(this).data('id')
    })

    $('#product-dataTables').on('click', '.edit-item-btn', function () {
        window.location.href = "/product/update/" + $(this).data('id')
    })

    determineGeneralUserRoleForAdd()
})