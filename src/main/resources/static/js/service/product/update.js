$(document).ready(function () {
    const token = getCsrfTokenCookie()

    $('#productForm').submit(function() {
        event.preventDefault()

        swal({
            title: "Are you sure, you want to save the data?",
            text: "",
            icon: "warning",
            buttons: true,
            dangerMode: true,
            }).then((value) => {
                if (value) processUpdate(token)
        }, "warning")
    })
})

function processUpdate(token) {
    if (currentUrlPath.includes("add")) 
        updateProduct('post', token)
    else updateProduct('put', token)
}

async function updateProduct(httpMethod, csrfToken) {
    let requestBody = {
        name : $('#productName').val(),
        code : $('#productCode').val(),
        unit : $('#unit').val(),
        categoryDto : {
            id : parseInt($('#selectCategory').val()),
            name : $('#selectCategory option:selected').text()
        },
        stock : $('#totalStock').val(),
        pricePerUnit: $('#pricePerUnit').val(),
        description: $('#description').val()
    }

    if (productId != null && parseInt(productId) > 0) 
        requestBody.id = productId

    let formData = new FormData()
    if ($('#productImage')[0].files[0] != null)
        formData.append("image", $('#productImage')[0].files[0])
    else if (imageByteaFromRepo != null)
        requestBody.image = imageByteaFromRepo

    formData.append(
            "data", new Blob([JSON.stringify(requestBody)], {type: "application/json"})
    )
    
    await $.ajax({
        url: '/api/v1/product',
        type: httpMethod,
        data: formData,
        processData: false,
        contentType: false,
        headers: {
            'X-XSRF-TOKEN': csrfToken
        },
        success: function(result) {
            baseResponse = result
            let resultData = result.data

            if (baseResponse.responseData.responseCode == 200) {
                clearData()
                swal({
                        title: "Saved Successfully!",
                        icon: "success",
                        closeOnClickOutside: true,
                        closeOnEsc: true,
                        button: "Redirect to Product List Menu",
                        onClose: function () {
                            window.location.href = "/product"
                        }
                    }).then((value) => {
                        if (value) window.location.href = "/product"
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

function clearData() {
    $('#productName').val("")
    $('#productCode').val("")
    $('#unit').val("")
    $('#selectCategory').val("")
    $('#totalStock').val(0)
    $('#pricePerUnit').val(0)
    $('#description').val("")
}

function validate() {
    if ($('#productName').val() == "") {
        swal("Attention!", "Product Name must not be empty", "warning")
        return false;
    }
        
    if ($('#unit').val() == "") {
        swal("Attention!", "Product Unit must not be empty", "warning")
        return false;
    }

    if ($('#selectCategory').val() == "") {
        swal("Attention!", "Please select category!", "warning")
        return false;
    }
    
    if ($('#pricePerUnit').val() == "" || $('#pricePerUnit').val() == 0) {
        swal("Attention!", "Price per unit must not be empty!", "warning")
        return false;
    }

    return true
}
