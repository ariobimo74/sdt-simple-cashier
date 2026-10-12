async function updateCategory(id, csrfToken) {
    event.preventDefault()
    if (parseInt(id) > 0) {
        swal({
            title: "Are you sure, you want to update the data?",
            text: "Once you saved, the updated data cannot be revert",
            icon: "warning",
            buttons: true,
            dangerMode: true,
            }).then((value) => {
                if (value) inputName(id, csrfToken)
        }, "warning")
    }    
    else inputName(id, csrfToken)
}

async function inputName(id, csrfToken) {
    event.preventDefault()
    swal("Input new category name (press Esc to cancel):", {
        content: "input",
        icon: "info",
        button: {
            closeModal: false,
        },
    })
    .then((value) => {
        if (value)
            saveData(id, csrfToken, value)
        else {
            swal.stopLoading()
            swal.close()
        }
    })
}

async function saveData(id, csrfToken, categoryName) {
    event.preventDefault()

    let requestBody = {
        id : id, 
        name : categoryName
    }

    await $.ajax({
        url: '/api/v1/category',
        type: 'patch',
        data: JSON.stringify(requestBody),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        headers: {
            'X-XSRF-TOKEN': token
        },
        success: function(result) {
            baseResponse = result
            let resultData = result.data

            if (baseResponse.responseData.responseCode == 200) {
                dataTable.ajax.reload()
                swal({
                        title: "Saved Successfully!",
                        icon: "success",
                        closeOnClickOutside: true,
                        closeOnEsc: true,
                        button: "OK",
                })
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
