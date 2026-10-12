package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDetailDto extends EditUserProfileDto {

    private String firstName;
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = IApplicationConstant.CommonValue.TimeZone.DEFAULT)
    private Date dateOfBirth;
}
