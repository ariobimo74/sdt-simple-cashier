package id.co.softwaredeveloperstoday.cms.dashboard.web.service.impl;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dao.RoleDao;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.RoleDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.mapper.RoleMapper;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.Role;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.RoleService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleName;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    private final RoleMapper roleMapper;

    @Override
    public RoleDto getRoleByRoleName(ERoleName name) {
        return roleMapper.convertRole(roleDao.findRoleByRoleName(name));
    }

    @Override
    public List<RoleDto> getRoles(Integer page, Integer size) {
        Page<Role> roles = roleDao.findAll(PageRequest.of(
                Optional.ofNullable(page).orElse(1) - 1,
                Optional.ofNullable(size).orElse(10)
        ));

        if (roles.getContent().isEmpty()) throw new DataNotFoundException(
                IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND
        );

        return roles.get().map(roleMapper::convertRole).collect(Collectors.toList());
    }

}
