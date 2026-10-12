$(document).ready(function() {
    let profileId = window.location.pathname.replace("profile/", "")

    generateContentForForm()
    changeHtmlType()
    makeInputsReadonly()
    getProfileData(profileId)

    function changeHtmlType() {
        $('#genderFormGroup').empty().append(`
            <label>Gender</label>
            <input type="text" class="form-control" id="gender" name="gender" >
        `)

        $('#roleFormGroup').empty().append(`
            <label>Role</label>
            <input type="text" class="form-control" id="role" name="role" >
        `)
    }

    function makeInputsReadonly() {
        $('form :input').each(function() {
            $(this).prop('readonly', true);
        });
    }
})