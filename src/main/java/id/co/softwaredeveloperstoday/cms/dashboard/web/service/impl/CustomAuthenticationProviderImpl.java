package id.co.softwaredeveloperstoday.cms.dashboard.web.service.impl;

import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.User;
import id.co.softwaredeveloperstoday.cms.dashboard.web.scope.UserScope;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.UserService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomAuthenticationProviderImpl implements AuthenticationProvider {

    private final PasswordEncoder encoder;

    private final UserService userService;

    private final UserScope userScope;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userService.findUserByUserName(username);
        if (Objects.isNull(user)) throw new UsernameNotFoundException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_USER);

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (encoder.matches(password, user.getPassword())) {
            user.getUserRoles().forEach(
                    r -> authorities.add(new SimpleGrantedAuthority(r.getRole().getRoleName().name()))
            );
            userScope.setUser(user);
            return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authorities);
        }
        else throw new UsernameNotFoundException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_USER);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
