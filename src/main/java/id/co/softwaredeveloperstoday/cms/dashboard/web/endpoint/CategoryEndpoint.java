package id.co.softwaredeveloperstoday.cms.dashboard.web.endpoint;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.CategoryDataTableDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.CategoryDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.ResponseDataTableDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.CategoryService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.DataNotFoundException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.UserNotAllowedException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.builder.ResultBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(
        value = IApplicationConstant.RestVersion.Category.CATEGORY_V1,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class CategoryEndpoint {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ResponseDataTableDto<CategoryDataTableDto>> getCategoriesDataTable(
            Authentication authentication,
            @RequestParam int draw,
            @RequestParam int start,
            @RequestParam int length,
            @RequestParam(required = false) String search
    ) {
        return new ResponseEntity<>(categoryService.getAll(authentication, draw, search, (start / length) + 1, length),
                HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<ResultDto<CategoryDto>> patchCategory(
            Authentication authentication, @RequestBody @Valid CategoryDto categoryDto
    ) {
        try {
            return ResultBuilderUtil.ok(categoryService.patchCategory(authentication, categoryDto));
        } catch (UserNotAllowedException e) {
            return ResultBuilderUtil.unauthorized();
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.notFound(e.getMessage());
        }
    }

    @DeleteMapping(
            path = IApplicationConstant.RestVersion.Category.ITEM_ID,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResultDto<Boolean>> deleteCategory(
            Authentication authentication, @PathVariable(value = IApplicationConstant.CommonValue.CommonRestPath.ID) Long id
    ) {
        try {
            categoryService.deleteCategoryByEditingIsDelete(authentication, id);
            return ResultBuilderUtil.ok(true);
        } catch (UserNotAllowedException e) {
            return ResultBuilderUtil.unauthorized();
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.notFound(false, e.getMessage());
        }
    }


}
