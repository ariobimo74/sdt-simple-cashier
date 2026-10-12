package id.co.softwaredeveloperstoday.cms.dashboard.web.factory;

import id.co.softwaredeveloperstoday.cms.dashboard.web.service.AuthorizationRoleLevelService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleLevel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthorizationRoleLevelFactory {

    private final Map<ERoleLevel, AuthorizationRoleLevelService> roleLevelServiceMap = new HashMap<>();

    public AuthorizationRoleLevelFactory(List<AuthorizationRoleLevelService> levelService) {
        for (AuthorizationRoleLevelService service : levelService) {
            roleLevelServiceMap.put(service.determineRoleLevel(), service);
        }
    }

    public AuthorizationRoleLevelService determineService(ERoleLevel roleLevel) {
        return roleLevelServiceMap.get(roleLevel);
    }

}
