package id.co.softwaredeveloperstoday.cms.dashboard.web.service;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.ResponseMenuDto;
import org.springframework.security.core.Authentication;

public interface MenuService {

    ResponseMenuDto getMenus(Authentication authentication);

}
