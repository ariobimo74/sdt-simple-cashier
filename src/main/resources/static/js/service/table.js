$(document).ready(function () {
    var currentRoles = []
    var allowedUserToChangePassword = []
    roleToDoSpecificAction()

    const dataTable = $('#user-dataTables').DataTable({
        responsive: true,
        processing: true,
        serverSide: true,
        ajax: {
            url: '/api/v1/user/profiles-data-table',
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
            { data: 'username' },
            { data: 'roleDto.roleName' },
            { data: 'email' },
            {
                data: 'id', 
                render: function (data, type, row) {
                    if (currentRoles.length > 0 && allowedUserToChangePassword.length > 0 
                        && currentRoles.some(data => allowedUserToChangePassword.includes(data))) 
                        return `
                            <button class="btn btn-primary detail-btn" title="View Detail" data-id="${data}"><i class="fa fa-fw" aria-hidden="true">&#xf15c</i></button>
                            <button class="btn btn-warning edit-btn" title="Edit Data" data-id="${data}"><i class="fa fa-fw" aria-hidden="true" >&#xf044</i></button>
                            <button class="btn btn-danger delete-btn" title="Delete Data" data-id="${data}"><i class="fa fa-fw" aria-hidden="true" >&#xf1f8</i></button>
                        `
                    else {
                        $('#register-btn').hide()
                        return `<center><button class="btn btn-primary detail-btn" title="View Detail" data-id="${data}"><i class="fa fa-fw" aria-hidden="true">&#xf15c</i></button></center>`
                    }
                },
                orderable: false, 
                searchable: false
            }
        ]
    })

    $('#user-dataTables').on('click', '.detail-btn', function () {
        window.location.href = "/profile/" + $(this).data('id')
    })

    $('#user-dataTables').on('click', '.edit-btn', function () {
        window.location.href = "/edit/" + $(this).data('id')
    })

    $('#register-btn').click(function () {
        window.location.href = "/register"
    })

    $('#user-dataTables').on('click', '.delete-btn', function () {
        event.preventDefault()

        swal({
            title: "Are you sure, you want to delete the data?",
            text: "",
            icon: "warning",
            buttons: true,
            dangerMode: true,
            }).then((value) => {
                if (value) deleteUser($(this).data('id'))
        }, "warning")
    })

    const token = getCsrfTokenCookie()

    async function deleteUser(profileId) {
        $.ajax({
            url: '/api/v1/user/profile/' + profileId,
            type: 'delete',
            contentType: 'application/json; charset=utf-8',
            headers: {
                'X-XSRF-TOKEN': token
            },
            success: function(result) {
                baseResponse = result    
                if (baseResponse.responseData.responseCode == 200) {
                    swal(result.responseData.acknowledge, "Successfully Deleted Data", "success")
                    dataTable.ajax.reload()
                }
                else swal(result.responseData.acknowledge, result.responseData.responseMessage, "error")
            }, 
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("jqXHR = ", jqXHR)
                console.log("status = ", jqXHR.status)
                console.log("textStatus = ", textStatus)
    
                baseResponse = jqXHR.responseJSON
                
                if (parseInt(jqXHR.status) == parseInt(404)) window.location.href = "/error/404"
                swal(baseResponse.responseData.acknowledge, baseResponse.responseData.responseMessage, "error")
            }
        })
    }

    async function roleToDoSpecificAction() {
        await $.ajax({
            url: '/api/v1/user/current-role',
                type: 'get',
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                success: function(result) {
                    currentRoles = result.data
                }
        })

        await $.ajax({
            url: '/api/v1/user/allowed-user-to-create-edit-user',
                type: 'get',
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                success: function(result) {
                    allowedUserToChangePassword = result.data
                }
        })
    }
})