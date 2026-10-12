var totalPriceTemp = 0
let table
var currentOrderId = 0
var paidTransaction = false 
var imageByteaFromRepo
var sendMail = false

$(document).ready(function () {
    determinePath()
    table = $('#ordered-items-dataTables').DataTable()

    $('#cashPaidByCustomer').on('input', function() {
        currencyFormatted($(this).val().replace(/[^\d.-]/g, ''), '#cashPaidByCustomerWithCurrency')
        readOnlyChange($(this).val().replace(/[^\d.-]/g, ''), '#cashPaidByCustomerWithCurrency')
    })

    $('#orderForm').submit(function() {
        event.preventDefault()

        swal({
            title: "Are you sure, you want to save the data?",
            text: "",
            icon: "warning",
            buttons: true,
            dangerMode: true,
            }).then((value) => {
                if (value) 
                    determineSendEmail(currentOrderId)
                else sendMail = false
        }, "warning")        
    })

    $('#previewOrderImage').css({
        display: 'none',
        marginTop: '10px',
        maxWidth: '300px',
        maxHeight: '300px',
        border: '1px solid #ddd',
        padding: '5px'
    })

    $('#orderImage').on('change', function () {
        const file = this.files[0]

        if (!file) {
            $('#previewOrderImage').hide()
            return
        }

        const reader = new FileReader()
        reader.onload = function (e) {
            $('#previewOrderImage')
                .attr('src', e.target.result)
                .show()
        }
        reader.readAsDataURL(file)
    })

    $('#removeImageBtn').on('click', function () {
        imageByteaFromRepo = null
        $('#orderImage').val('')
        $('#previewOrderImage')
            .attr('src', '')
            .hide()
    })

    $('#downloadBtn').click(function() {
        downloadPdf(currentOrderId)
    })

    $('#submitAndSendMailBtn').click(function() {
        sendMail = true
    })
})

async function determinePath() {
    currentUrlPath = window.location.pathname
    const pathArray = currentUrlPath.split("/")
    currentOrderId = pathArray[pathArray.length - 1]
    await retrieveDetailOrder(currentOrderId, pathArray[pathArray.length - 2])
    if (currentUrlPath.includes("detail")) detailProcessDisabled()
    else if (paidTransaction) paidTransactionDisabled()
}

async function retrieveDetailOrder(orderId, activity) {
    await $.ajax({
        url: '/api/v1/order/detail/' + orderId,
        type: 'get',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function(result) {
            baseResponse = result
            let resultData = result.data

            if (baseResponse.responseData.responseCode == 200) {
                paidTransaction = resultData.status != null && resultData.status == "PAID" 
                if (activity.includes("payment") && !paidTransaction) 
                    swal("Payment", "Successfully checkout! Please continue the payment!", "info")

                $('#orderNumber').val(resultData.orderNumber)

                $('#totalPrice').val(resultData.totalPriceWithCurrency)
                totalPriceTemp = resultData.totalPrice

                $('#cashPaidByCustomer').val(resultData.cashPaidByCustomer)
                $('#orderStatus').val(resultData.status)
                $('#totalOrderedItem').val(resultData.totalOrderedItem)
                $('#notes').val(resultData.notes)
                $('#changeWithCurrency').val(resultData.changeWithCurrency)

                $('#cashPaidByCustomerWithCurrency').val(resultData.cashPaidByCustomerWithCurrency)
                $('#changeWithCurrency').val(resultData.changeWithCurrency)

                let availablePaymentMethod = resultData.availablePaymentMethods
                if (availablePaymentMethod.length > 0) {
                    $('#paymentMethod').empty()
                    for (i = 0; i < availablePaymentMethod.length; i++) {
                        $('#paymentMethod').append('<option val="' + availablePaymentMethod[i] + '">' + availablePaymentMethod[i] + '</option>')
                    }
                }
                $('#paymentMethod').val(resultData.paymentMethod)

                const orderItemDtoList = resultData.orderItemDtoList
                if (orderItemDtoList.length > 0) {
                    for (i = 0; i < orderItemDtoList.length; i++) {
                        const orderedItem = orderItemDtoList[i]
                        table.row.add([
                            i + 1,
                            orderedItem.productName,
                            orderedItem.currentPricePerItemUnitWithCurrency,
                            orderedItem.totalItem,
                            orderedItem.sumTotalAllItemPriceWithCurrency
                        ])
                    }
                    table.draw()
                }

                currencyFormatted(resultData.pricePerUnit, '#cashPaidByCustomerWithCurrency')

                if (resultData.attachment != null) imageByteaFromRepo = resultData.attachment

                if (resultData.encodedAttachment != null) {
                    $('#previewOrderImage').attr(
                        "src",
                        "data:image/jpeg;base64," + resultData.encodedAttachment
                    )
                    $('#previewOrderImage').show()
                }
            }
            else swal(result.responseData.acknowledge, result.responseData.responseMessage, "error")
        }, 
        error: function(jqXHR, textStatus, errorThrown) {
            console.log("jqXHR = ", jqXHR)
            console.log("status = ", jqXHR.status)
            console.log("textStatus = ", textStatus)

            baseResponse = jqXHR.responseJSON
            
            if (parseInt(jqXHR.status) == parseInt(404)) {
                if (activity.includes("payment")) {
                    swal("successfully checkout!", "checkout has been successfully proceed! Please continue the payment", "warning")
                    setTimeout(function() {
                        retrieveDetailOrder(orderId, activity)
                    }, 5000)
                }
                else window.location.href = "/error/404"
            }
            swal(baseResponse.responseData.acknowledge, baseResponse.responseData.responseMessage, "error")
        }
    })
}

function currencyFormatted(price, targetInputFormattedValue) {
    if (!isNaN(price) && price !== '')
    $(targetInputFormattedValue).val(
        new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'IDR'
        }).format(price)
    )
}

function readOnlyChange(inputCustomerCash) {
    const change = parseFloat(inputCustomerCash) - parseFloat(totalPriceTemp)
    currencyFormatted(change, '#changeWithCurrency')
}

async function payment(orderId, emailAddress) {
    const requestBody = {
        orderId : parseInt(orderId), 
        paidByCustomer : $('#cashPaidByCustomer').val(), 
        paymentMethod : $('#paymentMethod option:selected').text(), 
        notes : $('#notes').val(), 
        sendMail : sendMail, 
        emailAddress : emailAddress
    }

    if (imageByteaFromRepo != null) requestBody.attachment = imageByteaFromRepo

    let formData = new FormData()
    if ($('#orderImage')[0].files[0] != null)
        formData.append("image", $('#orderImage')[0].files[0])
        formData.append(
            "data", new Blob([JSON.stringify(requestBody)], {type: "application/json"})
    )

    await $.ajax({
        url: '/api/v1/order/payment',
        type: 'patch',
        data: formData,
        processData: false,
        contentType: false,
        headers: {
            'X-XSRF-TOKEN': getCsrfTokenCookie()
        },
        success: function(result) {
            baseResponse = result    
            if (baseResponse.responseData.responseCode == 200) {
                if (!sendMail)
                    downloadPdf(orderId)
                sendMail = false
                swal({
                        title: "Order Payment is saved successfully!",
                        icon: "success",
                        closeOnClickOutside: true,
                        closeOnEsc: true,
                        button: "OK",
                        onClose: function () {
                            disableSubmitButton() 
                        }
                    }).then((value) => {
                        disableSubmitButton() 
                })
            }
            else swal(result.responseData.acknowledge, result.responseData.responseMessage, "error")
        }, 
        error: function(jqXHR, textStatus, errorThrown) {
            console.log("jqXHR = ", jqXHR)
            console.log("status = ", jqXHR.status)
            console.log("textStatus = ", textStatus)
            
            swal("An issue is occured", "Failed to submit payment with sending email. Please use submission without sending email!", "info")
            swal.stopLoading()
            swal.close()

            baseResponse = jqXHR.responseJSON
            
            if (parseInt(jqXHR.status) == parseInt(404)) swal(baseResponse.responseData.acknowledge, baseResponse.responseData.responseMessage, "error")
            swal(baseResponse.responseData.acknowledge, baseResponse.responseData.responseMessage, "warning")
        }
    })
}

function detailProcessDisabled() {
    paidTransactionDisabled()
    $('#notes').prop("readonly", true)

    $('#removeImageBtn').css("display", "none")
    $('#uploadImageGroup').css("display", "none")
    $('#submitBtn').css("display", "none")
    $('#submitAndSendMailBtn').css("display", "none")

    $('#downloadBtn').css("display", "inline")
}

function paidTransactionDisabled() {
    $('#cashPaidByCustomer').prop("readonly", true)
    $('#paymentMethod').prop("disabled", true)
}

function disableSubmitButton() {
    $('#orderStatus').val("PAID")
    $('#submitBtn').prop("disabled", true)
    $('#submitAndSendMailBtn').prop("disabled", true)
}

async function determineSendEmail(orderId) {
    if (sendMail) 
        confirmSendMail(orderId)
    else payment(orderId)
}

async function confirmSendMail(orderId) {
    swal("Please input email address (press Esc to cancel):", {
        content: "input",
        icon: "info",
        button: {
            closeModal: false,
        },
    })
    .then((value) => {
        if (value)
            payment(orderId, value)
        else {
            swal.stopLoading()
            swal.close()
        }
    })
}
