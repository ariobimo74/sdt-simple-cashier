$('document').ready(function() {
    generateContentForForm()
    generatePasswordMatcher()
    generateCommonValues()

    $('#firstName').on('change', function() {
        getRecommendedUsername()
    })
    $('#birthDate').on('change', function() {
        getRecommendedUsername()
    })

    $('#registerForm').submit(function() {
        event.preventDefault()

        if ($('#firstName').val() == "" || $('#idCardNumber').val() == "" || $('#birthDate').val() == "" || $('#birthPlace').val() == ""
            || $('#gender').val() == "" || $('#username').val() == "" || $('#password').val() == "" || $('#confirmPassword').val() == "" 
            || $('#role').val() == "") {
                swal("Attention!", "Please insert the reqired fields!", "info")
                return false
        }
        
        let registerRequestBody = {
            name : $('#firstName').val() + " " + $('#lastName').val(),
            idCardNumber : $('#idCardNumber').val(),
            dateOfBirth : $('#birthDate').val(),
            placeOfBirth : $('#birthPlace').val(),
            gender : $('#gender').val(),
            username : $('#username').val(),
            password : $('#password').val(),
            confirmPassword : $('#confirmPassword').val(),
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
                if (value) executeSaveUser('post', '/api/v1/register', registerRequestBody, true)
        }, "warning")
    })

    $(document).on('click', '#recommendedUsername .btn', function() {
        var value = $(this).attr('name');
        $('#username').val(value);
    });
    
})
