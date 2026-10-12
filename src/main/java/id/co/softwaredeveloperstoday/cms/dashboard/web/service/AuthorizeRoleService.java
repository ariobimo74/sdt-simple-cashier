package id.co.softwaredeveloperstoday.cms.dashboard.web.service;

import org.springframework.security.core.Authentication;

public interface AuthorizeRoleService {

    void authorizeSuperAdmin(Authentication authentication);

    void authorizeRegularAdmin(Authentication authentication);

}
