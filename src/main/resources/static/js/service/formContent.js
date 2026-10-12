async function generateContentForForm() {
    $('#formContent').empty().append(`
                        <div class="form-group">
                            <div class="row">
                                <div class="col-lg-6">
                                    <p class="text-left">First name</p>
                                    <input type="text" class="form-control" id="firstName" name="firstName" placeholder="First Name" required>
                                </div>
                                <div class="col-lg-6">
                                    <p class="text-left">Last name</p>
                                    <input type="text" class="form-control" id="lastName" name="lastName" placeholder="Last Name">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <p class="text-left">Id Card Number or Passport</p>
                            <input type="number" class="form-control" id="idCardNumber" name="idCardNumber" placeholder="Id Card Number or Passport" min="0" step="1" required>
                        </div>

                        <div class="form-group" id="genderFormGroup">
                            <label>Gender</label>
                            <select class="form-control" id="gender">
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                            </select>
                        </div>
                        <div class="form-group" id="roleFormGroup">
                            <label>Role</label>
                            <select class="form-control" id="role">
                                <option>Select Role</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <p class="text-left">Phone Number</p>
                            <input type="number" class="form-control" id="phoneNumber" name="phoneNumber" placeholder="Phone Number" min="0" step="1">
                        </div>
                        <div class="form-group">
                            <p class="text-left">Email</p>
                            <input type="email" class="form-control" id="email" name="email" placeholder="Email">
                        </div>

                        <div class="form-group">
                            <p class="text-left">Place Of Birth</p>
                            <input type="text" class="form-control" id="birthPlace" name="birthPlace" placeholder="Place of Birth" required>
                        </div>
                        <div class="form-group">
                            <p class="text-left">Date Of Birth</p>
                            <input type="date" class="form-control" id="birthDate" name="birthDate" placeholder="Date of Birth" required>
                        </div>

                        <div class="form-group">
                            <p class="text-left">Username</p>
                            <input type="text" class="form-control" id="username" name="username" placeholder="Username to be used to login to dashboard" readonly required>
                        </div>

                        <div class="form-group" id="recommendedUsername" style="display: none">
                        </div>
                    `)
}