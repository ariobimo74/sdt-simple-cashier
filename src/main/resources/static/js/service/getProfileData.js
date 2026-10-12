async function getProfileData(profileId) {
    await $.ajax({
        url: '/api/v1/user/profile' + profileId,
        type: 'get',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function(result) {
            baseResponse = result
            let resultData = result.data

            if (baseResponse.responseData.responseCode == 200) {
                currentUsername = resultData.username
                $('#firstName').val(resultData.firstName)
                $('#lastName').val(resultData.lastName)
                $('#idCardNumber').val(resultData.idCardNumber)
                $('#birthDate').val(resultData.dateOfBirth)
                $('#birthPlace').val(resultData.placeOfBirth)
                $('#gender').val(resultData.gender)
                $('#username').val(resultData.username)
                $('#role').val(resultData.roleDto.roleName)
                $('#phoneNumber').val(resultData.mobilePhoneNumber)
                $('#email').val(resultData.email)
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