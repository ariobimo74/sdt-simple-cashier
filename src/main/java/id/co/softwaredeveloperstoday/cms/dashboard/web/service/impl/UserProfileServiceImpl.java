package id.co.softwaredeveloperstoday.cms.dashboard.web.service.impl;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dao.RoleDao;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dao.UserProfileDao;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.mapper.RoleMapper;
import id.co.softwaredeveloperstoday.cms.dashboard.web.mapper.UserProfileDtoMapper;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.Role;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.User;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.UserProfile;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.UserRole;
import id.co.softwaredeveloperstoday.cms.dashboard.web.scope.UserProfileScope;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.AuthorizeRoleService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.UserProfileService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.common.PageableDataTableCommonUtil;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EDataTableSortBy;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EUserSortBy;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.DataNotFoundException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.PasswordNotMatchException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserProfileServiceImpl implements UserProfileService {
    private final PasswordEncoder encoder;

    private final UserProfileDao userProfileDao;
    private final RoleDao roleDao;

    private final UserProfileDtoMapper userProfileDtoMapper;
    private final RoleMapper roleMapper;

    private final UserProfileScope userProfileScope;

    private final AuthorizeRoleService authorizeRoleService;

    @Override
    public AddUserProfileDto addUserProfile(Authentication authentication, AddUserProfileDto userProfileDto) {
        authorizeRoleService.authorizeSuperAdmin(authentication);

        if (!Objects.equals(userProfileDto.getPassword(), userProfileDto.getConfirmPassword()))
            throw new PasswordNotMatchException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_NEW_PASSWORD_NOT_MATCH);

        Role role = roleMapper.convertRoleDto(userProfileDto.getRoleDto());
        User user = userProfileDtoMapper.convertUser(userProfileDto);
        UserRole userRole = new UserRole(null, user, role);

        userRole.setCreatedDate(new Date());
        userRole.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        user.setUserRoles(Collections.singletonList(userRole));
        user.setPassword(encoder.encode(userProfileDto.getPassword()));
        user.setCreatedDate(new Date());
        user.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        UserProfile userProfile = userProfileDtoMapper.convertUserProfile(userProfileDto);
        userProfile.setUser(user);
        userProfile.setCreatedDate(new Date());
        userProfile.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        return userProfileDtoMapper.convertAddUserProfileDto(userProfileDao.save(userProfile));
    }

    @Override
    public UserProfileDetailDto findUserById(Authentication authentication, Long userProfileId) {
        UserProfile userProfile = userProfileDao.findById(userProfileId).orElseThrow(() -> new DataNotFoundException(
                IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND)
        );
        if (!Objects.equals(userProfile.getUser().getUsername(), authentication.getName()))
            authorizeRoleService.authorizeRegularAdmin(authentication);

        userProfileScope.setUserProfile(userProfile);
        return userProfileDtoMapper.convertUserProfileDetailDto(userProfileScope.getUserProfile());
    }

    @Override
    public UserProfileDetailDto updateUserProfile(Authentication authentication, EditUserProfileDto editUserProfileDto) {
        authorizeRoleService.authorizeSuperAdmin(authentication);
        UserProfile userProfile;

        if (Objects.nonNull(userProfileScope) && Objects.nonNull(userProfileScope.getUserProfile())
                && Objects.nonNull(userProfileScope.getUserProfile().getId()))
            userProfile = userProfileScope.getUserProfile();
        else userProfile = userProfileDao.findById(editUserProfileDto.getId()).orElseThrow(
                () -> new DataNotFoundException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_USER_NOT_FOUND)
        );

        List<UserRole> userRoles = userProfile.getUser().getUserRoles().stream().map(
                ur -> {
                    if (Objects.equals(ur.getId(), userProfile.getUser().getUserRoles().stream().findFirst().orElseThrow().getId())) {
                        Role role = roleDao.findById(editUserProfileDto.getRoleDto().getId()).orElse(ur.getRole());

                        ur.setRole(role);
                        ur.setModifiedDate(new Date());
                        ur.setModifiedBy(authentication.getName());

                        return ur;
                    } else return ur;
                }
        ).distinct().collect(Collectors.toList());

        userProfile.getUser().setUserRoles(userRoles);
        userProfile.setDateOfBirth(editUserProfileDto.getDateOfBirth());
        userProfile.setName(editUserProfileDto.getName());
        userProfile.setIdCardNumber(editUserProfileDto.getIdCardNumber());
        userProfile.setPlaceOfBirth(editUserProfileDto.getPlaceOfBirth());
        userProfile.setGender(editUserProfileDto.getGender());
        userProfile.getUser().setUsername(editUserProfileDto.getUsername().trim());
        userProfile.setMobilePhoneNumber(editUserProfileDto.getMobilePhoneNumber());
        userProfile.setEmail(editUserProfileDto.getEmail());
        userProfile.setPhotoUrl(editUserProfileDto.getPhotoUrl());
        userProfile.setMemberLevel(editUserProfileDto.getMemberLevel());

        return userProfileDtoMapper.convertUserProfileDetailDto(userProfileDao.save(userProfile));
    }

    @Override
    public Page<UserProfileDetailDto> getAllPaging(
            Authentication authentication, String searchName, String searchUsername, String searchDate, Integer page, Integer size, EUserSortBy sortBy, boolean isAscendingSort
    ) {
        authorizeRoleService.authorizeRegularAdmin(authentication);
        Date searchDateFormatted = null;
        if (Objects.nonNull(searchDate)) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                searchDateFormatted = dateFormat.parse(searchDate);
            } catch (ParseException e) {
                log.info("Handled Date Parsing Exception : " + searchDate);
            }
        }

        Page<UserProfile> userProfiles = userProfileDao.findAll(
                specificationUserProfile(
                        searchName, searchUsername, searchDateFormatted), PageableDataTableCommonUtil.pageable(page, size, sortBy.getName(), isAscendingSort
                )
        );

        if (userProfiles.getTotalElements() == 0)
            throw new DataNotFoundException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND);
        else return userProfiles.map(userProfileDtoMapper::convertUserProfileDetailDto);
    }

    @Override
    public ResponseDataTableDto<UserProfileDataTableDto> getAllPagingDataTable(Authentication authentication, int draw, String search, Integer page, Integer size, EDataTableSortBy dataTableSortBy, boolean isAscendingSort) {
        authorizeRoleService.authorizeRegularAdmin(authentication);
        Page<UserProfile> userProfiles;
        if (StringUtils.isBlank(search))
            userProfiles = userProfileDao.findAll(PageableDataTableCommonUtil.pageable(page, size, dataTableSortBy.getName(), isAscendingSort));
        else userProfiles = userProfileDao.findAll(
                specificationUserProfileDataTable(search), PageableDataTableCommonUtil.pageable(page, size, dataTableSortBy.getName(), isAscendingSort)
        );

        if (userProfiles.getTotalElements() == 0)
            return new ResponseDataTableDto<>(draw, 0, 0, new ArrayList<>());
        else return new ResponseDataTableDto<>(draw, userProfileDao.count(), userProfiles.getTotalElements(),
                userProfiles.getContent().stream().map(userProfileDtoMapper::convertUserProfileDataTableDto).collect(Collectors.toList())
        );
    }

    @Override
    public EditUserProfileDto deleteUserByEditingIsDelete(Authentication authentication, Long id) {
        authorizeRoleService.authorizeSuperAdmin(authentication);
        UserProfile userProfile = userProfileDao.findById(id).orElseThrow(
                () -> new DataNotFoundException(
                        IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND
        ));
        userProfile.setIsDelete(true);
        userProfile.getUser().setIsDelete(true);

        return userProfileDtoMapper.convertEditUserProfileDto(userProfileDao.save(userProfile));
    }

    private Specification<UserProfile> specificationUserProfile(String searchName, String searchUsername, Date searchDate) {
        return (root, query, criteriaBuilder) -> {
            Join<UserProfile, User> userJoin = root.join("user", JoinType.LEFT);
            Predicate userPredicate = criteriaBuilder.conjunction();
            Predicate userProfilePredicate = criteriaBuilder.conjunction();

            if (Objects.nonNull(searchDate))
                userProfilePredicate = criteriaBuilder.equal(root.get("dateOfBirth"), searchDate);

            if (Objects.nonNull(searchName))
                userProfilePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), '%' + searchName.toLowerCase() + '%');

            if (Objects.nonNull(searchUsername))
                userPredicate = criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("username")), '%' + searchUsername.toLowerCase() + '%');

            return criteriaBuilder.and(userPredicate, userProfilePredicate);
        };
    }

    private Specification<UserProfile> specificationUserProfileDataTable(String search) {
        return (root, query, criteriaBuilder) -> {
            Join<UserProfile, User> userJoin = root.join("user", JoinType.LEFT);
            Join<User, UserRole> userRoleJoin = userJoin.join("userRoles", JoinType.LEFT);
            Join<UserRole, Role> roleJoin = userRoleJoin.join("role", JoinType.LEFT);

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, root.get("dateOfBirth"), criteriaBuilder.literal("YYYY-MM-DD")), '%' + search + '%'),
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), '%' + search.toLowerCase() + '%'),
                            criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("username")), '%' + search.toLowerCase() + '%')
                    ),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("idCardNumber")), '%' + search.toLowerCase() + '%'),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("placeOfBirth")), '%' + search.toLowerCase() + '%'),
                    criteriaBuilder.equal(root.get("gender").as(String.class), search.toUpperCase()),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), '%' + search.toLowerCase() + '%'),
                    criteriaBuilder.like(root.get("memberLevel").as(String.class), '%' + search.toUpperCase() + '%'),
                    criteriaBuilder.like(criteriaBuilder.lower((roleJoin.get("roleName").as(String.class))), '%' + search.toLowerCase() + '%'),
                    criteriaBuilder.like(criteriaBuilder.lower((roleJoin.get("roleName").as(String.class))), '%' + search.toLowerCase().replace(StringUtils.SPACE, "_") + '%')
            );
        };
    }

}
