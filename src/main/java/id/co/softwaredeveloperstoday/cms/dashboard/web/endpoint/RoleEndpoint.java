package id.co.softwaredeveloperstoday.cms.dashboard.web.endpoint;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.RoleDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.RoleService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleName;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.DataNotFoundException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultListDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.builder.ResultBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = IApplicationConstant.RestVersion.Role.ROLE_V1
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleEndpoint {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<ResultListDto<RoleDto>> getRoles(
            @RequestParam(
                    value = IApplicationConstant.CommonValue.CommonRestParam.PAGE,
                    required = false, defaultValue = "1") Integer page,
            @RequestParam(
                value = IApplicationConstant.CommonValue.CommonRestParam.SIZE,
                required = false, defaultValue = "10") Integer size
    ) {
        try {
            return ResultBuilderUtil.ok(roleService.getRoles(page, size));
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.noContent();
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(IApplicationConstant.CommonValue.CommonRestPath.FIND_BY_NAME)
    public ResponseEntity<ResultDto<RoleDto>> getRoleByRoleName(
            @RequestParam(
                    value = IApplicationConstant.CommonValue.CommonRestParam.FIND_BY_NAME,
                    required = false, defaultValue = "USER") ERoleName roleName
    ) {
        try {
            return ResultBuilderUtil.ok(roleService.getRoleByRoleName(roleName));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return ResultBuilderUtil.internalServerError(e.getMessage());
        }
    }

}
