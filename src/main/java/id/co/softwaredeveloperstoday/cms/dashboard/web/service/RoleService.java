package id.co.softwaredeveloperstoday.cms.dashboard.web.service;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.RoleDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleName;

import java.util.List;

public interface RoleService {

    RoleDto getRoleByRoleName(ERoleName name);

    List<RoleDto> getRoles(Integer page, Integer size);

}
