package id.co.softwaredeveloperstoday.cms.dashboard.web.endpoint;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.AddUserProfileDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.UserProfileService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.PasswordNotMatchException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.UserNotAllowedException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.builder.ResultBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(
        value = IApplicationConstant.RestVersion.Register.REGISTER_V1
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class RegisterEndpoint {

    private final UserProfileService userProfileService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResultDto<AddUserProfileDto>> registerUser(
            Authentication authentication,
            @RequestBody @Valid AddUserProfileDto userProfileDto
    ) {
        try {
            return ResultBuilderUtil.ok(userProfileService.addUserProfile(authentication, userProfileDto));
        } catch (PasswordNotMatchException e) {
            return ResultBuilderUtil.badRequest(null, e.getMessage());
        } catch (UserNotAllowedException e) {
            return ResultBuilderUtil.unauthorized();
        } catch (Exception e) {
            return ResultBuilderUtil.internalServerError(e.getMessage());
        }
    }

}
