package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EGenderType;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EMemberLevel;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {

    @NotNull
    @NotBlank
    @NotEmpty
    private String name;
    @NotNull
    @NotBlank
    @NotEmpty
    private String idCardNumber;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = IApplicationConstant.CommonValue.TimeZone.DEFAULT)
    private Date dateOfBirth;
    @NotNull
    @NotBlank
    @NotEmpty
    private String placeOfBirth;
    @NotNull
    private EGenderType gender;
    @NotNull
    @NotBlank
    @NotEmpty
    private String username;

    @NotNull
    private RoleDto roleDto = new RoleDto(
            Long.parseLong(String.valueOf(ERoleName.USER.ordinal())), ERoleName.USER
    );

    private String mobilePhoneNumber;
    private String email;
    private String photoUrl;
    private EMemberLevel memberLevel = EMemberLevel.REGULAR;

}
