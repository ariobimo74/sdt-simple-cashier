package id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EDataTableSortBy {
    NAME("name"), USERNAME("user.username"), ROLE("user.userRoles.role.roleName"), EMAIL("email");

    private final String name;
}
