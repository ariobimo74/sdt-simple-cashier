let requestCheckoutData = []
let table
var timer

$(document).ready(function () {
    table = $('#checkout-result-dataTables').DataTable({ordering: false})
    $('#notificationPanelsHere').css({
        "position": "fixed",
        "bottom": "20px",
        "left": "20px",
        "z-index": 2
    })

    const token = getCsrfTokenCookie()

    $('#product-order-dataTables').on('click', '.btn-add-to-cart', function () {
        clearTimeout(timer)
        addProductOrderWithNotif($(this).data('id'))
        timer = setTimeout(function () {
            refreshProductOrderCheckout(token)
        }, 500)
    })

    $('#checkout-result-dataTables').on('click', '.btn-reduce-product-order', function () {
        reduceProductOrder($(this).data('id'))
        refreshProductOrderCheckout(token)
    })

    $('#checkout-result-dataTables').on('click', '.btn-add-product-order', function () {
        addProductOrder($(this).data('id'))
        refreshProductOrderCheckout(token)
    })

    $('#checkout-result-dataTables').on('click', '.btn-remove-product-order', function () {
        removeProductOrder($(this).data('id'))
        refreshProductOrderCheckout(token)
    })

    $('#checkoutBtn').click(function () {
        checkout()
    })

    const orderStatus = $('#orderStatus').val()
    if (orderStatus == "UNPAID") $('#submitBtn').text("Payment and save")
})

function addProductOrder(productOrderId) {
    if (requestCheckoutData.length > 0) {
        for (i = 0; i < requestCheckoutData.length; i++) {
            let productOrder = requestCheckoutData[i]

            if (parseInt(productOrder.productId) == parseInt(productOrderId)){
                productOrder.totalItem = parseInt(parseInt(productOrder.totalItem) + 1)
                return productOrder.totalItem
            }
        }
        requestCheckoutData.push({productId: parseInt(productOrderId), totalItem: 1})
    }
    else requestCheckoutData.push({productId: parseInt(productOrderId), totalItem: 1})
    return 1
}

async function refreshProductOrderCheckout(csrfToken) {
    table.clear()

    await $.ajax({
        url: '/api/v1/order/calculate',
        type: 'post',
        data: JSON.stringify(requestCheckoutData),
        headers: {
            'X-XSRF-TOKEN': csrfToken
        },
        contentType: 'application/json; charset=utf-8',
        success: function(result) {
            baseResponse = result    
            if (baseResponse.responseData.responseCode == 200) {
                requestCheckoutData = []
                const orderItemDataList = baseResponse.data.orderItemDtoList
                for(i = 0; i < orderItemDataList.length; i++) {
                    const productOrder = orderItemDataList[i]
                    requestCheckoutData.push(
                        {
                            productId: parseInt(productOrder.productId), 
                            totalItem: productOrder.totalItem}
                        )
                    const removeProductButton = 
                                    '<button type="button" class="btn btn-danger btn-remove-product-order" data-id="' + productOrder.productId + '">' +
                                        '<i class="fa fa-trash fa-fw"></i>' +
                                    '</button>'
                    const dataButton = '<button type="button" class="btn btn-danger btn-reduce-product-order" data-id="' + productOrder.productId + '">' +
                                            '<i class="fa fa-minus fa-fw"></i>' +
                                        '</button>' +
                                        '<input style="text-align: center" id="pricePerUnit" value="' + productOrder.totalItem + '" class="form-control" type="number" placeholder="" min="0" readOnly>' +
                                        '<button type="button" class="btn btn-success btn-add-product-order" data-id="' + productOrder.productId + '">' +
                                            '<i class="fa fa-plus fa-fw"></i>' +
                                        '</button>' 
                    const generatedImage = generateImage(productOrder.image)

                    table.row.add([
                        removeProductButton,
                        i + 1,
                        generatedImage,
                        productOrder.productName,
                        productOrder.currentPricePerItemUnitWithCurrency,
                        dataButton,
                        productOrder.sumTotalAllItemPriceWithCurrency
                    ])
                }

                $('#totalItem').text(baseResponse.data.totalOrderedItem)
                $('#totalPrice').text(baseResponse.data.totalPriceWithCurrency)
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

    table.draw()
}

function reduceProductOrder(productOrderId) {
    if (requestCheckoutData.length > 0) {
        for (i = 0; i < requestCheckoutData.length; i++) {
            let productOrder = requestCheckoutData[i]

            if (parseInt(productOrder.productId) == parseInt(productOrderId)){
                if (parseInt(productOrder.totalItem) > 1) 
                    productOrder.totalItem = parseInt(productOrder.totalItem) - 1
                else requestCheckoutData.splice(i, 1)
            }
        }
    }
}

function addProductOrderWithNotif(productOrderId) {
    const addedProduct = addProductOrder(productOrderId)
    $('#notificationPanelsHere').append(
        `
        <div class="alert alert-success alert-dismissible">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            Successfully added product data to cart: ` + addedProduct + `
        </div>
        `
    )

    $('.alert-success').delay(5000).fadeOut(500)
}

function removeProductOrder(productOrderId) {
    if (requestCheckoutData.length > 0) {
        for (i = 0; i < requestCheckoutData.length; i++) {
            let productOrder = requestCheckoutData[i]

            if (parseInt(productOrder.productId) == parseInt(productOrderId)){
                requestCheckoutData.splice(i, 1)
            }
        }
    }
}

function generateImage(byteImage) {
    if (byteImage != null)
        return '<center><img class="previewImageProductOrder" alt="Image Preview" src="data:image/png;base64,' + byteImage + '" style="marginTop: 1px; width: 40px; height: 80px; border: 1px solid #ddd; padding: 5px"></center>'
    else return '<center><img class="previewImageProductOrder" alt="Image Preview" src="" style="display: none;"></center>'
}

async function checkout() {
    await $.ajax({
        url: '/api/v1/order/checkout',
        type: 'post',
        data: JSON.stringify(requestCheckoutData),
        headers: {
            'X-XSRF-TOKEN': getCsrfTokenCookie()
        },
        contentType: 'application/json; charset=utf-8',
        success: function(result) {
            baseResponse = result    
            if (baseResponse.responseData.responseCode == 200) {
                requestCheckoutData = []
                window.location.href = "/order/payment/" + baseResponse.data.id
            }
            else swal(result.responseData.acknowledge, result.responseData.responseMessage, "error")
        }, 
        error: function(jqXHR, textStatus, errorThrown) {
            console.log("jqXHR = ", jqXHR)
            console.log("status = ", jqXHR.status)
            console.log("textStatus = ", textStatus)

            baseResponse = jqXHR.responseJSON
            
            if (parseInt(jqXHR.status) == parseInt(404)) swal(baseResponse.responseData.acknowledge, baseResponse.responseData.responseMessage, "error")
            swal(baseResponse.responseData.acknowledge, baseResponse.responseData.responseMessage, "warning")
        }
    })
}
