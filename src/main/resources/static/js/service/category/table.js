var token
var dataTable

$(document).ready(function () {
    token = getCsrfTokenCookie()
    
    dataTable = $('#category-dataTables').DataTable({
        ordering: false,
        responsive: true,
        processing: true,
        serverSide: true,
        ajax: {
            url: '/api/v1/category',
            data: function (d) {
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
            {
                data: 'actionsId', 
                render: function (data, type, row) {
                    if (data != null) {
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
                    } else return ""
                },
                searchable: false
            }
        ]
    })

    $('#add-category-btn').click(function () {
        updateCategory(0, token)
    })

    $('#category-dataTables').on('click', '.edit-item-btn', function () {
        updateCategory($(this).data('id'), token)
    })

    determineGeneralUserRoleForAdd()
})