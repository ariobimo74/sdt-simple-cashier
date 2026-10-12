package id.co.softwaredeveloperstoday.cms.dashboard.web.service;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EDataTableSortBy;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EUserSortBy;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface UserProfileService {

    AddUserProfileDto addUserProfile(Authentication authentication, AddUserProfileDto userProfileDto);

    UserProfileDetailDto findUserById(Authentication authentication, Long userProfileId);

    UserProfileDetailDto updateUserProfile(Authentication authentication, EditUserProfileDto editUserProfileDto);

    Page<UserProfileDetailDto> getAllPaging(Authentication authentication, String searchName, String searchUsername, String searchDate, Integer page, Integer size, EUserSortBy sortBy, boolean isAscendingSort);

    ResponseDataTableDto<UserProfileDataTableDto> getAllPagingDataTable(Authentication authentication, int draw, String search, Integer page, Integer size, EDataTableSortBy dataTableSortBy, boolean isAscendingSort);

    EditUserProfileDto deleteUserByEditingIsDelete(Authentication authentication, Long id);

}
