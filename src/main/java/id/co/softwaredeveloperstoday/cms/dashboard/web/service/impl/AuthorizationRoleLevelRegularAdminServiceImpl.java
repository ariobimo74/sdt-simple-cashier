package id.co.softwaredeveloperstoday.cms.dashboard.web.service.impl;

import id.co.softwaredeveloperstoday.cms.dashboard.web.service.AuthorizationRoleLevelService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleLevel;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleName;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationRoleLevelRegularAdminServiceImpl implements AuthorizationRoleLevelService {

    @Override
    public boolean isAuthorized(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(
                a -> a.toString().equals(ERoleName.SUPER_ADMIN.toString())
                        || a.toString().equals(ERoleName.ADMIN.toString())
        );
    }

    @Override
    public ERoleLevel determineRoleLevel() {
        return ERoleLevel.REGULAR_ADMIN;
    }

}
