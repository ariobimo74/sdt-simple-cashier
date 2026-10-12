package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDataTableDto {

    private Long id;
    private Long userId;
    private String name;
    private String username;
    private RoleDto roleDto = new RoleDto(
            Long.parseLong(String.valueOf(ERoleName.USER.ordinal())), ERoleName.USER
    );
    private String email;

}
