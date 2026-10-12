package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.validator.StrongPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestChangePasswordDto {

    private String username;
    @NotNull
    @NotBlank
    @NotEmpty
    private String oldPassword;
    @StrongPassword
    @NotNull
    @NotBlank
    @NotEmpty
    private String newPassword;
    @NotNull
    @NotBlank
    @NotEmpty
    private String confirmNewPassword;

}
