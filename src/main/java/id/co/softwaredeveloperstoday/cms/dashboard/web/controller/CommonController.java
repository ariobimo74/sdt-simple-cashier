package id.co.softwaredeveloperstoday.cms.dashboard.web.controller;

import id.co.softwaredeveloperstoday.cms.dashboard.web.scope.UserScope;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.AuthorizeRoleService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.UserNotAllowedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommonController {

    private final UserScope userScope;

    private final AuthorizeRoleService authorizeRoleService;

    @GetMapping("/login")
    public String loginPage() {
        if (Objects.isNull(userScope) || Objects.isNull(userScope.getUser())
                || Objects.isNull(userScope.getUser().getUsername()))
            return "login/login";
        else return "dashboard";
    }

    @GetMapping("/register")
    public String registerPage(Authentication authentication) {
        try {
            authorizeRoleService.authorizeSuperAdmin(authentication);
            return "login/register";
        } catch (UserNotAllowedException e) {
            return "error/404";
        }
    }

    @GetMapping
    public String defaultDashboard() {
        return dashboard();
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/change_password")
    public String forgotPasswordPage() {return "login/change_password";}

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie sessionCookie = new Cookie("JSESSIONID", null);
        Cookie rememberMeCookie = new Cookie("softwaredeveloperstoday-remember-me", null);

        String cookiePath = StringUtils.hasText(request.getContextPath()) ? request.getContextPath() : "/";

        sessionCookie.setPath(cookiePath);
        sessionCookie.setMaxAge(0);
        sessionCookie.setSecure(request.isSecure());
        rememberMeCookie.setPath(cookiePath);
        rememberMeCookie.setMaxAge(0);
        rememberMeCookie.setSecure(request.isSecure());

        response.addCookie(sessionCookie);
        response.addCookie(rememberMeCookie);

        return "login/login";
    }

    @GetMapping("/error/404")
    public String handleError() {
        return "error/404";
    }

    @GetMapping(IApplicationConstant.RestVersion.User.VIEW_USER_PROFILE)
    public String viewUserProfile(Authentication authentication) {
        return "user/detail";
    }

    @GetMapping(IApplicationConstant.RestVersion.User.EDIT_USER_PROFILE)
    public String editUserProfile(Authentication authentication) {
        try {
            authorizeRoleService.authorizeSuperAdmin(authentication);
            return "user/edit";
        } catch (UserNotAllowedException e) {
            return "error/404";
        }
    }

    @GetMapping(IApplicationConstant.RestVersion.User.VIEW_USER_PROFILES)
    public String viewUserProfiles(Authentication authentication) {
        try {
            authorizeRoleService.authorizeRegularAdmin(authentication);
            return "user/table";
        } catch (UserNotAllowedException e) {
            return "error/404";
        }
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/company-profile")
    public String companyProfile() {
        return "company";
    }

}
