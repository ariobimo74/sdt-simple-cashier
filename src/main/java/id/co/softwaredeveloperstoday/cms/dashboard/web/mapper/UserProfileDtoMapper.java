package id.co.softwaredeveloperstoday.cms.dashboard.web.mapper;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.CompanyProfile;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.User;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.UserProfile;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.UserRole;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleName;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public abstract class UserProfileDtoMapper {

    public abstract UserProfile convertUserProfile(AddUserProfileDto userProfileDto);

    public abstract User convertUser(AddUserProfileDto addUserProfileDto);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "password", source = "user.password")
    public abstract AddUserProfileDto convertAddUserProfileDto(UserProfile userProfile);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "roleDto", expression = "java(determineOneUserRole(userProfile.getUser().getUserRoles()))")
    @Mapping(target = "firstName", expression = "java(splitName(userProfile.getName(), 0))")
    @Mapping(target = "lastName", expression = "java(splitName(userProfile.getName(), 1))")
    public abstract UserProfileDetailDto convertUserProfileDetailDto(UserProfile userProfile);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "roleDto", expression = "java(determineOneUserRole(userProfile.getUser().getUserRoles()))")
    public abstract UserProfileDataTableDto convertUserProfileDataTableDto(UserProfile userProfile);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "roleDto", expression = "java(determineOneUserRole(userProfile.getUser().getUserRoles()))")
    public abstract EditUserProfileDto convertEditUserProfileDto(UserProfile userProfile);

    public abstract CompanyProfileDto convertCompanyProfile(CompanyProfile companyProfile);

    String splitName(String fullName, int index) {
        if (Objects.isNull(fullName)) return "";

        try {
            String[] names = fullName.split(" ");

            if (index == 0) {
                return names[index];
            } else {
                if (names.length > 2) {
                    StringBuilder resultName = new StringBuilder();
                    for (int i = 1; i < names.length; i++) {
                        resultName.append(names[i]).append(" ");
                    }

                    return resultName.toString();
                } else return names[index];
            }
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    RoleDto determineOneUserRole(List<UserRole> userRoles) {
        return userRoles.stream().findFirst().map(role -> new RoleDto(role.getRole().getId(), role.getRole().getRoleName()))
                .orElse(new RoleDto((long) ERoleName.USER.ordinal(), ERoleName.USER));
    }

}
