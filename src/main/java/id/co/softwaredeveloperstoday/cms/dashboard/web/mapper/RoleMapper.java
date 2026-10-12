package id.co.softwaredeveloperstoday.cms.dashboard.web.mapper;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.RoleDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RoleMapper {

    public abstract RoleDto convertRole(Role role);

    public abstract Role convertRoleDto(RoleDto roleDto);

}
