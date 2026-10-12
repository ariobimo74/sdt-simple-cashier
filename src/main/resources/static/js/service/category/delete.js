$(document).ready(function() {
    $('#category-dataTables').on('click', '.delete-item-btn', function () {
        event.preventDefault()

        swal({
            title: "Are you sure, you want to delete the data?",
            text: "",
            icon: "warning",
            buttons: true,
            dangerMode: true,
            }).then((value) => {
                if (value) deleteCategory($(this).data('id'))
        }, "warning")
    })
})

async function deleteCategory(id) {
    await $.ajax({
        url: '/api/v1/category/' + id,
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