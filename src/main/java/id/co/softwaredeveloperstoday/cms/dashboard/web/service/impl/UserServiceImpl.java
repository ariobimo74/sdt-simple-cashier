package id.co.softwaredeveloperstoday.cms.dashboard.web.service.impl;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dao.UserDao;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dao.UserReplicaDao;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.RequestChangePasswordDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.factory.AuthorizationRoleLevelFactory;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.User;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.UserReplica;
import id.co.softwaredeveloperstoday.cms.dashboard.web.scope.UserScope;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.AuthorizationRoleLevelService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.UserService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleLevel;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.PasswordNotMatchException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.UserNotAllowedException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final PasswordEncoder encoder;

    private final UserDao userDao;
    private final UserReplicaDao userReplicaDao;

    private final UserScope userScope;

    private final AuthorizationRoleLevelFactory roleLevelFactory;

    @Override
    public User findUserByUserName(String username) {
        return userDao.findByUsernameIgnoreCase(username);
    }

    @Override
    public List<UserReplica> getUserByUsernameIn(List<String> usernames) {
        return userReplicaDao.findByUsernameIn(usernames);
    }

    @Override
    public String changePassword(Authentication authentication, RequestChangePasswordDto changePasswordDto) {
        AuthorizationRoleLevelService regularAdminLevelService = roleLevelFactory.determineService(ERoleLevel.REGULAR_ADMIN);
        AuthorizationRoleLevelService superAdminLevelService = roleLevelFactory.determineService(ERoleLevel.SUPER_ADMIN);

        if (StringUtils.isNotBlank(changePasswordDto.getUsername())
                && !regularAdminLevelService.isAuthorized(authentication))
            throw new UserNotAllowedException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_USER_NOT_ALLOWED);

        User user;
        if (StringUtils.isNotBlank(changePasswordDto.getUsername()))
            user = userDao.findByUsernameIgnoreCase(changePasswordDto.getUsername());
        else if (StringUtils.isBlank(changePasswordDto.getUsername()) && Objects.nonNull(userScope)
                && Objects.nonNull(userScope.getUser()) && Objects.nonNull(userScope.getUser().getUsername())
                && Objects.equals(userScope.getUser().getUsername(), authentication.getName()))
            user = userScope.getUser();
        else user = userDao.findByUsernameIgnoreCase(authentication.getName());

        if (Objects.isNull(user))
            throw new UsernameNotFoundException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_USER_FOUND_USERNAME);

        if (!superAdminLevelService.isAuthorized(authentication) && !encoder.matches(changePasswordDto.getOldPassword(), user.getPassword()))
            throw new PasswordNotMatchException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_OLD_PASSWORD_NOT_MATCH);

        if (!Objects.equals(changePasswordDto.getNewPassword(), changePasswordDto.getConfirmNewPassword()))
            throw new PasswordNotMatchException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_NEW_PASSWORD_NOT_MATCH);

        String encodedOldPassword = encoder.encode(changePasswordDto.getNewPassword());
        user.setPassword(encodedOldPassword);
        user.setModifiedDate(new Date());
        user.setModifiedBy(authentication.getName());
        user.setPasswordmodifiedDate(new Date());
        user.setPasswordmodifiedBy(authentication.getName());

        userDao.save(user);

        return IApplicationConstant.CommonValue.RestResponseValue.SUCCESS;
    }

}
