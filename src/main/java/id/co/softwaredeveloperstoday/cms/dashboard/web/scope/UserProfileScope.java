package id.co.softwaredeveloperstoday.cms.dashboard.web.scope;


import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.UserProfile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
public class UserProfileScope {

    private UserProfile userProfile;

}
