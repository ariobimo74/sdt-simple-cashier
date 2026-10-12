package id.co.softwaredeveloperstoday.cms.dashboard.web.dao;

import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.Role;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role, Long> {

    Role findRoleByRoleName(ERoleName roleName);

    Page<Role> findAll(Pageable pageable);

}
