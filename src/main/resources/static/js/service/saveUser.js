async function executeSaveUser(method, url, jsonData, clearInput) {
    const token = getCsrfTokenCookie()

    await $.ajax({
        url: url,
        type: method,
        data: JSON.stringify(jsonData),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        headers: {
            'X-XSRF-TOKEN': token
        },
        success: function(result) {
            baseResponse = result

            if (baseResponse.responseData.responseCode == 200) {

                if (clearInput) {
                    $('#firstName').val('')
                    $('#lastName').val('')
                    $('#idCardNumber').val('')
                    $('#birthDate').val('')
                    $('#birthPlace').val('')
                    $('#gender').val('')
                    $('#username').val('')
                    $('#password').val('')
                    $('#confirmPassword').val('')
                    $('#role').val('')
                    $('#phoneNumber').val('')
                    $('#email').val('')
                }

                swal({
                        title: "Saved Successfully!",
                        icon: "success",
                        closeOnClickOutside: true,
                        closeOnEsc: true,
                        button: "Redirect to User Profiles Menu",
                        onClose: function () {
                            window.location.href = "/profiles"
                        }
                    }).then((value) => {
                        if (value) window.location.href = "/profiles"
                    })
            }
            else swal(result.responseData.acknowledge, result.responseData.responseMessage, "error")
        }, 
        error: function(jqXHR, textStatus, errorThrown) {
            console.log("jqXHR = ", jqXHR)
            console.log("status = ", jqXHR.status)
            console.log("textStatus = ", textStatus)

            baseResponse = jqXHR.responseJSON
            
            swal(baseResponse.responseData.acknowledge, baseResponse.responseData.responseMessage, "error")
        }
    })
}