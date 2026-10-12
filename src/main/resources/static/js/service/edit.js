$(document).ready(function() {
    let profileId = window.location.pathname.replace("edit/", "")
    editPreparation()

    async function editPreparation() {
        await generateContentForForm()
        await generateCommonValues()
        await getProfileData(profileId)

        if (baseResponse.data.roleDto != null && baseResponse.data.roleDto.id != null) 
            $('#role').val(baseResponse.data.roleDto.id)
    }

    $('#firstName').on('change', function() {
        getRecommendedUsername()
    })
    $('#birthDate').on('change', function() {
        getRecommendedUsername()
    })

    $('#editForm').submit(function() {
        event.preventDefault()

        if ($('#firstName').val() == "" || $('#idCardNumber').val() == "" || $('#birthDate').val() == "" || $('#birthPlace').val() == ""
            || $('#gender').val() == "" || $('#username').val() == "" || $('#role').val() == "") {
                swal("Attention!", "Please insert the reqired fields!", "info")
                return false
        }
        
        let editRequestBody = {
            id : parseInt(profileId.replace('/','')),
            name : $('#firstName').val() + " " + $('#lastName').val(),
            idCardNumber : $('#idCardNumber').val(),
            dateOfBirth : $('#birthDate').val(),
            placeOfBirth : $('#birthPlace').val(),
            gender : $('#gender').val(),
            username : $('#username').val(),
            roleDto : {
                id : $('#role').val(),
                roleName : $('#role option:selected').text()
            },
            mobilePhoneNumber : $('#phoneNumber').val(),
            email : $('#email').val()
        }

        swal({
            title: "Are you sure, you want to save the data?",
            text: "",
            icon: "warning",
            buttons: true,
            dangerMode: true,
            }).then((value) => {
                if (value) executeSaveUser('put', '/api/v1/user', editRequestBody, false)
        }, "warning")
    })
})