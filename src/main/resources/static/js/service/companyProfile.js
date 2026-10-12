var mailElementId

$(document).ready(function() {
    generateCompanyProfile()

    $(document).on("click", ".update-profile", function(e) {
        e.preventDefault()

        swal({
            title: "Are you sure, you want to save the data?",
            text: "",
            icon: "warning",
            buttons: true,
            dangerMode: true,
            }).then((value) => {
                if (value) updateCompanyProfile($(this).data('id'))
        }, "warning")

    })
})

async function generateCompanyProfile() {
    event.preventDefault()

    await $.ajax({
        url: '/api/v1/value/company-profile',
        type: 'get',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function(result) {
            baseResponse = result
            let resultData = result.data

            let formResult = ""
            if (baseResponse.responseData.responseCode == 200) {
                for (let i = 0; i < resultData.length; i++) {
                    const element = resultData[i];
                    const isHtmlPreview = element.htmlPreview

                    const inputType = element.inputType

                    let textAreaClosing = ""
                    if (inputType != "input") textAreaClosing = `${element.content}</${inputType}>`

                    if (!isHtmlPreview)
                        formResult += `
                            <div class="row">
                                <div class="col-lg-8">
                                    <div class="form-group">
                                        <label>${element.label}</label>
                                        <${inputType} id="element-${element.id}" type="text" value="${element.content}" name="company-profile-${element.id}" class="form-control">
                                        ${textAreaClosing}
                                    </div>
                                </div>
                                <div class="col-lg-2">
                                    <div class="form-group">
                                        <button type="button" class="btn btn-warning update-profile" data-id="${element.id}">
                                            <i class="fa fa-fw fa-pencil" aria-hidden="true" ></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        `
                    else formResult += `
                            <div class="row">
                                <div class="col-lg-8">
                                    <div class="form-group">
                                        <label>${element.label}</label>
                                        <${inputType} id="element-${element.id}" value="${element.content}" name="company-profile-${element.id}" class="form-control" readonly>
                                        ${textAreaClosing}
                                    </div>
                                </div>
                                <div class="col-lg-2">
                                    <div class="form-group">
                                        <button type="button" class="btn btn-warning update-profile" data-id="${element.id}" disabled>
                                            <i class="fa fa-fw fa-pencil" aria-hidden="true" ></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        `
                    
                    if (isHtmlPreview && inputType == "textarea") {
                        if (element.name == "EMAIL_CONTENT") {
                            mailElementId = "element-" + element.id 
                            formResult += `
                                <div class="row">
                                    <div class="col-lg-8">
                                        <div class="form-group">
                                            <label>Preview Homepage Content Result</label>
                                            <div id="email-content">
                                                ${element.content}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            `
                        }
                    }
                }
                $('#companyProfileForm').append(formResult)
            }
            else swal(result.responseData.acknowledge, result.responseData.responseMessage, "error")
            listenContentChange()
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

async function updateCompanyProfile(id) {
    let requestBody = {
        id : id,
        content : $('#element-' + id).val()
    }

    await $.ajax({
        url: '/api/v1/value/company-profile',
        type: 'put',
        data: JSON.stringify(requestBody),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        headers: {
            'X-XSRF-TOKEN': getCsrfTokenCookie()
        },
        success: function(result) {
            baseResponse = result
            let resultData = result.data

            if (baseResponse.responseData.responseCode == 200)
                swal({
                        title: "Saved Successfully!",
                        icon: "success",
                        closeOnClickOutside: true,
                        closeOnEsc: true,
                        button: "Ok",
                        onClose: function () {
                            location.reload()
                        }
                    }).then((value) => {
                        if (value) location.reload()
                })
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

function listenContentChange() {
    $('#' + mailElementId).on('input', function() {
        const emailInputValue = $(this).val()
        $('#email-content').empty()
        $('#email-content').html(emailInputValue)
    })
}
