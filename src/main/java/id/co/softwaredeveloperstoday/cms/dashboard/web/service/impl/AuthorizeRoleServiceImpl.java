package id.co.softwaredeveloperstoday.cms.dashboard.web.service.impl;

import id.co.softwaredeveloperstoday.cms.dashboard.web.factory.AuthorizationRoleLevelFactory;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.AuthorizationRoleLevelService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.AuthorizeRoleService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleLevel;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.UserNotAllowedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizeRoleServiceImpl implements AuthorizeRoleService {

    private final AuthorizationRoleLevelFactory roleLevelFactory;

    @Override
    public void authorizeSuperAdmin(Authentication authentication) {
        AuthorizationRoleLevelService roleLevelService = roleLevelFactory.determineService(ERoleLevel.SUPER_ADMIN);
        if (!roleLevelService.isAuthorized(authentication))
            throw new UserNotAllowedException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_USER_NOT_ALLOWED);
    }

    @Override
    public void authorizeRegularAdmin(Authentication authentication) {
        AuthorizationRoleLevelService roleLevelService = roleLevelFactory.determineService(ERoleLevel.REGULAR_ADMIN);
        if (!roleLevelService.isAuthorized(authentication))
            throw new UserNotAllowedException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_USER_NOT_ALLOWED);
    }

}
