package id.co.softwaredeveloperstoday.cms.dashboard.web.service.impl;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.MenuRoleDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.MenuDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.ResponseMenuDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.scope.UserProfileScope;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.MenuService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MenuServiceImpl implements MenuService {

    private final UserProfileScope userProfileScope;

    @Override
    public ResponseMenuDto getMenus(Authentication authentication) {
        List<MenuRoleDto> menuRoles = new ArrayList<>();
        menuRoles.add(new MenuRoleDto(
                "Dashboard", "dashboard", "fa fa-dashboard fa-fw", "/dashboard", 1,
                        List.of(ERoleName.SUPER_ADMIN, ERoleName.ADMIN, ERoleName.USER), List.of()
                )
        );
        menuRoles.add(new MenuRoleDto("Company Profile", "company-profile", "fa fa-building fa-fw", "/company-profile", 2,
                        List.of(ERoleName.SUPER_ADMIN), List.of()));
        menuRoles.add(new MenuRoleDto("User List", "user-list", "fa fa-users fa-fw", "/profiles", 3,
                        List.of(ERoleName.SUPER_ADMIN, ERoleName.ADMIN), List.of()));
        menuRoles.add(new MenuRoleDto("Change Password", "change-password", "fa fa-key fa-fw", "/change_password", 4,
                        List.of(ERoleName.SUPER_ADMIN, ERoleName.ADMIN, ERoleName.USER), List.of()));
        menuRoles.add(new MenuRoleDto("Product Category", "category", "fa fa-tag fa-fw", "/category", 5,
                        List.of(ERoleName.SUPER_ADMIN, ERoleName.ADMIN, ERoleName.USER), List.of()));
        menuRoles.add(new MenuRoleDto("Our Products", "our-product", "fa fa-home fa-fw", "/product", 6,
                        List.of(ERoleName.SUPER_ADMIN, ERoleName.ADMIN, ERoleName.USER), List.of()));
        menuRoles.add(new MenuRoleDto("Order and Payment", "order-payment", "fa fa fa-cart-arrow-down fa-fw", "/order", 7,
                        List.of(ERoleName.SUPER_ADMIN, ERoleName.ADMIN, ERoleName.USER), List.of()));
        menuRoles.add(new MenuRoleDto("Transaction History", "order-history", "fa fa fa-history fa-fw", "/order/history", 8,
                        List.of(ERoleName.SUPER_ADMIN, ERoleName.ADMIN, ERoleName.USER), List.of()));
        menuRoles.add(new MenuRoleDto(
                "About", "about", "fa fa-newspaper-o fa-fw", "/about", 9,
                List.of(ERoleName.SUPER_ADMIN, ERoleName.ADMIN, ERoleName.USER), List.of()
        ));
        menuRoles.add(new MenuRoleDto(
                "<span style=\"color: red\">Log Out</span>", "log-out", "fa fa-sign-out fa-fw", "/logout", 10,
                List.of(ERoleName.SUPER_ADMIN, ERoleName.ADMIN, ERoleName.USER), List.of()
        ));

        menuRoles.stream().filter(m -> !m.getMenuRoles().isEmpty()).forEach(
                m -> m.setSubMenu(
                        m.getMenuRoles().stream().filter(mr -> authentication.getAuthorities().stream().map(String::valueOf).anyMatch(
                                        a -> mr.getRoles().stream().map(String::valueOf).collect(Collectors.toList()).contains(a)
                                )
                        ).map(
                                mr -> new MenuDto(mr.getMenuName(), mr.getMenuClass(), mr.getIconClass(), mr.getUrl(),
                                        mr.getSubMenu(), mr.getOrderNumber())
                        ).collect(Collectors.toList())
                )
        );

        String greetingName = authentication.getName();
        if (Objects.nonNull(userProfileScope) && Objects.nonNull(userProfileScope.getUserProfile())
                && Objects.nonNull(userProfileScope.getUserProfile().getId()))
            greetingName = userProfileScope.getUserProfile().getName();

        return new ResponseMenuDto(greetingName, menuRoles.stream().filter(
                m -> authentication.getAuthorities().stream().map(String::valueOf).anyMatch(
                        a -> m.getRoles().stream().map(String::valueOf).collect(Collectors.toList()).contains(a)
                )
        ).map(
                m -> new MenuDto(m.getMenuName(), m.getMenuClass(),
                        m.getIconClass(), m.getUrl(), m.getSubMenu(), m.getOrderNumber())
        ).collect(Collectors.toList()));
    }

}
