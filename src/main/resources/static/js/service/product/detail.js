$(document).ready(function () {
    prepareAwaitCall()
    
    $('#previewProductImage').css({
        display: 'none',
        marginTop: '10px',
        maxWidth: '300px',
        maxHeight: '300px',
        border: '1px solid #ddd',
        padding: '5px'
    })

    $('#productImage').on('change', function () {
        const file = this.files[0]

        if (!file) {
            $('#previewProductImage').hide()
            return
        }

        const reader = new FileReader()
        reader.onload = function (e) {
            $('#previewProductImage')
                .attr('src', e.target.result)
                .show()
        }
        reader.readAsDataURL(file)
    })

    $('#removeImageBtn').on('click', function () {
        imageByteaFromRepo = null
        $('#productImage').val('')
        $('#previewProductImage')
            .attr('src', '')
            .hide()
    })

    $('#pricePerUnit').on('input', function() {
        currencyFormatted($(this).val().replace(/[^\d.-]/g, ''))
    })
})

async function prepareAwaitCall() {
    await retrieveCategories()
    await determinePath()
}

async function retrieveCategories() {
    await $.ajax({
        url: '/api/v1/product/categories',
        type: 'get',
        contentType: 'application/json; charset=utf-8',
        success: function(result) {
            baseResponse = result    
            if (baseResponse.responseData.responseCode == 200) {
                for(i = 0; i < baseResponse.totalData; i++) {
                    let category = baseResponse.data[i]
                    $('#selectCategory').append(
                        "<option value='" + category.id + "'>" + category.name + "</option>"
                    )
                }
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

var productId = null
var currentUrlPath
var imageByteaFromRepo = null

async function determinePath() {
    currentUrlPath = window.location.pathname

    if (currentUrlPath.includes("update") || currentUrlPath.includes("detail")) {
        const pathArray = currentUrlPath.split("/")
        productId = pathArray[pathArray.length - 1]
        retrieveDetailProduct(productId)

        if (currentUrlPath.includes("detail")) {
            $('.form-control').prop("disabled", true)
            $('#submitBtn').hide()
            $('#cancelBtn').text('Close')
            $('#removeImageBtn').hide()
            $('#uploadImageGroup').hide()
        }
    }
}

async function retrieveDetailProduct(productId) {
    await $.ajax({
        url: '/api/v1/product/item/' + productId,
        type: 'get',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function(result) {
            baseResponse = result
            let resultData = result.data

            if (baseResponse.responseData.responseCode == 200) {
                $('#productName').val(resultData.name)
                $('#productCode').val(resultData.code)
                $('#unit').val(resultData.unit)

                $('#selectCategory').val(resultData.categoryDto.id)
                
                $('#totalStock').val(resultData.stock)
                $('#pricePerUnit').val(resultData.pricePerUnit)
                $('#description').val(resultData.description)

                currencyFormatted(resultData.pricePerUnit)

                if (resultData.image != null) imageByteaFromRepo = resultData.image

                if (resultData.encodedImage != null){
                    $('#previewProductImage').attr(
                        "src",
                        "data:image/jpeg;base64," + resultData.encodedImage
                    )
                    $('#previewProductImage').show()
                }
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

function currencyFormatted(price) {
    if (!isNaN(price) && price !== '')
    $('#formattedPricePerUnit').val(
        new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'IDR'
        }).format(price)
    )
}
