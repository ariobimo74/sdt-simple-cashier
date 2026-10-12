package id.co.softwaredeveloperstoday.cms.dashboard.web.service;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleLevel;
import org.springframework.security.core.Authentication;

public interface AuthorizationRoleLevelService {

    boolean isAuthorized(Authentication authentication);

    ERoleLevel determineRoleLevel();

}
