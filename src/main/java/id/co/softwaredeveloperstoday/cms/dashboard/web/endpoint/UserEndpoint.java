package id.co.softwaredeveloperstoday.cms.dashboard.web.endpoint;

import com.fasterxml.jackson.annotation.JsonFormat;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.UserProfileService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.UserService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EDataTableSortBy;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleName;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ESortType;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EUserSortBy;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.DataNotFoundException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.PasswordNotMatchException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.UserNotAllowedException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultListDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultPageDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.builder.ResultBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(
        value = IApplicationConstant.RestVersion.User.USER_V1,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class UserEndpoint {

    private final UserService userService;
    private final UserProfileService userProfileService;

    @GetMapping(IApplicationConstant.RestVersion.User.VIEW_USER_PROFILES)
    public ResponseEntity<ResultPageDto<UserProfileDetailDto>> getUserProfiles(
            Authentication authentication,
            @RequestParam(
                    value = IApplicationConstant.CommonValue.CommonRestParam.PAGE,
                    required = false, defaultValue = "1") Integer page,
            @RequestParam(
                    value = IApplicationConstant.CommonValue.CommonRestParam.SIZE,
                    required = false, defaultValue = "10") Integer size,
            @RequestParam(
                    value = IApplicationConstant.CommonValue.CommonRestParam.FIND_BY_NAME,
                    required = false) String searchName,
            @JsonFormat(shape = JsonFormat.Shape.STRING,
                    pattern = "yyyy-MM-dd", timezone = IApplicationConstant.CommonValue.TimeZone.DEFAULT)
            @RequestParam(
                    value = IApplicationConstant.CommonValue.CommonRestParam.FIND_BY_DATE_OF_BIRTH,
                    required = false) String searchDate,
            @RequestParam(
                    value = IApplicationConstant.CommonValue.CommonRestParam.FIND_BY_USER_NAME,
                    required = false) String searchUserName,
            @RequestParam(
                    value = IApplicationConstant.CommonValue.Pagination.SORT_BY_PARAMETER,
                    required = false, defaultValue = "NAME") EUserSortBy sortBy,
            @RequestParam(
                    value = IApplicationConstant.CommonValue.Pagination.SORT_TYPE_PARAMETER,
                    required = false, defaultValue = "ASC") ESortType sortType
            ) {
        try {
            return ResultBuilderUtil.ok(userProfileService.getAllPaging(authentication, searchName, searchUserName, searchDate, page, size, sortBy, Objects.equals(sortType, ESortType.ASC)));
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.noPageContent();
        } catch (UserNotAllowedException e) {
            return ResultBuilderUtil.unauthorizedPage();
        }
    }

    @GetMapping(IApplicationConstant.RestVersion.User.VIEW_USER_PROFILE)
    public ResponseEntity<ResultDto<UserProfileDetailDto>> getUserProfile(
            Authentication authentication, @PathVariable(IApplicationConstant.CommonValue.CommonRestPath.ID) Long id
    ) {
        try {
            return ResultBuilderUtil.ok(userProfileService.findUserById(authentication, id));
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.notFound(e.getMessage());
        } catch (UserNotAllowedException e) {
            return ResultBuilderUtil.unauthorized();
        }
    }

    @PostMapping(IApplicationConstant.RestVersion.User.CHANGE_PASSWORD)
    public ResponseEntity<ResultDto<String>> changePassword(
            Authentication authentication, @RequestBody @Valid RequestChangePasswordDto changePasswordDto
    ) {
        try {
            return ResultBuilderUtil.ok(userService.changePassword(authentication, changePasswordDto));
        } catch (UsernameNotFoundException | PasswordNotMatchException e) {
            return ResultBuilderUtil.badRequest(
                    IApplicationConstant.CommonValue.RestResponseValue.FAILED, e.getMessage()
            );
        } catch (UserNotAllowedException e) {
            return ResultBuilderUtil.unauthorized();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBuilderUtil.internalServerError(e.getMessage());
        }
    }

    @GetMapping(IApplicationConstant.RestVersion.User.CURRENT_ROLE)
    public ResponseEntity<ResultListDto<ERoleName>> getCurrentRoles(Authentication authentication) {
        return ResultBuilderUtil.ok(authentication.getAuthorities().stream().map(r -> ERoleName.valueOf(String.valueOf(r))).collect(Collectors.toList()));
    }

    @GetMapping(IApplicationConstant.RestVersion.User.CHANGE_PASSWORD_ALLOWED_USER)
    public ResponseEntity<ResultListDto<ERoleName>> getAllowedUserToChangePassword(Authentication authentication) {
        return ResultBuilderUtil.ok(List.of(ERoleName.SUPER_ADMIN));
    }

    @GetMapping(IApplicationConstant.RestVersion.User.CREATE_EDIT_ACTION_ALLOWED_USER)
    public ResponseEntity<ResultListDto<ERoleName>> getAllowedUserToCreateEditAction(Authentication authentication) {
        return ResultBuilderUtil.ok(List.of(ERoleName.SUPER_ADMIN));
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResultDto<UserProfileDetailDto>> updateUser(
            Authentication authentication, @Valid @RequestBody EditUserProfileDto editUserProfileDto
    ) {
        try {
            return ResultBuilderUtil.ok(userProfileService.updateUserProfile(authentication, editUserProfileDto));
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.notFound(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBuilderUtil.internalServerError(e.getMessage());
        }
    }

    @GetMapping(IApplicationConstant.RestVersion.User.VIEW_USER_PROFILES_DATA_TABLE)
    public ResponseEntity<ResponseDataTableDto<UserProfileDataTableDto>> getUserProfilesDataTable(
            Authentication authentication,
            @RequestParam int draw,
            @RequestParam int start,
            @RequestParam int length,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, value = "order[0][column]") int orderColumn,
            @RequestParam(required = false, value = "order[0][dir]") String orderDir
    ) {
        try {
            return new ResponseEntity<>(userProfileService.getAllPagingDataTable(authentication, draw, search, (start / length) + 1, length,
                    Stream.of(EDataTableSortBy.values()).filter(s -> s.ordinal() == orderColumn - 1).findFirst().orElse(EDataTableSortBy.NAME),
                    orderDir.equalsIgnoreCase(ESortType.ASC.toString())), HttpStatus.OK);
        } catch (DataNotFoundException | UserNotAllowedException e) {
            return null;
        }
    }

    @DeleteMapping(IApplicationConstant.RestVersion.User.VIEW_USER_PROFILE)
    public ResponseEntity<ResultDto<EditUserProfileDto>> deleteUser(
            Authentication authentication, @PathVariable(value = IApplicationConstant.CommonValue.CommonRestPath.ID) Long id
    ) {
        try {
            return ResultBuilderUtil.ok(userProfileService.deleteUserByEditingIsDelete(authentication, id));
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.badRequest(e.getMessage());
        } catch (UserNotAllowedException e) {
            return ResultBuilderUtil.unauthorized();
        }
    }

}
