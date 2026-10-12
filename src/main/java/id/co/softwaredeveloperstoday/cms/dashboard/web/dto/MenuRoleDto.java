package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuRoleDto extends MenuDto {

    private List<ERoleName> roles;
    private List<MenuRoleDto> menuRoles;

    public MenuRoleDto(String menuName, String menuClass, String iconClass, String url, int orderNumber, List<ERoleName> roles, List<MenuRoleDto> menuRoles) {
        super(menuName, menuClass, iconClass, url, List.of(), orderNumber);
        this.roles = roles;
        this.menuRoles = menuRoles;
    }
}
