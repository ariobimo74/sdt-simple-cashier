package id.co.softwaredeveloperstoday.cms.dashboard.web.endpoint;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.ResponseMenuDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.MenuService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.builder.ResultBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = IApplicationConstant.RestVersion.Menu.MENU_V1
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MenuEndpoint {

    private final MenuService menuService;

    @GetMapping()
    public ResponseEntity<ResultDto<ResponseMenuDto>> getMenuList(Authentication authentication) {
        try {
            return ResultBuilderUtil.ok(menuService.getMenus(authentication));
        } catch (Exception e) {
            return ResultBuilderUtil.internalServerError(e.getMessage());
        }
    }

}
