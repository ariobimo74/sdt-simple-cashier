package id.co.softwaredeveloperstoday.cms.dashboard.web.endpoint;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.CategoryService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.ProductService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EProductDataTableSortBy;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ESortType;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.DataNotFoundException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.UploadImageException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.UserNotAllowedException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultListDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.builder.ResultBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.stream.Stream;

@RestController
@RequestMapping(
        value = IApplicationConstant.RestVersion.Product.PRODUCT_V1
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class ProductEndpoint {

    private final CategoryService categoryService;

    private final ProductService productService;

    @GetMapping(
            path = IApplicationConstant.RestVersion.Product.VIEW_CATEGORIES,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResultListDto<CategoryDto>> getAllCategories() {
        try {
            return ResultBuilderUtil.ok(categoryService.getAll());
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.noContent();
        }
    }

    @GetMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseDataTableDto<ProductDataTableDto>> getProductsDataTable(
            Authentication authentication,
            @RequestParam int draw,
            @RequestParam int start,
            @RequestParam int length,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, value = "order[0][column]") int orderColumn,
            @RequestParam(required = false, value = "order[0][dir]") String orderDir
    ) {
        return new ResponseEntity<>(productService.getAllPagingDataTable(authentication, draw, search, (start / length) + 1, length,
                Stream.of(EProductDataTableSortBy.values()).filter(s -> s.ordinal() == orderColumn - 1).findFirst()
                        .orElse(EProductDataTableSortBy.NAME), orderDir.equalsIgnoreCase(ESortType.ASC.toString())), HttpStatus.OK);
    }

    @GetMapping(
            path = IApplicationConstant.RestVersion.Product.VIEW_ITEM_CODE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResultDto<UpdateProductDto>> getProductByCode(
            @PathVariable(IApplicationConstant.CommonValue.CommonRestPath.CODE) @NotNull @Valid String code
    ) {
        try {
            return ResultBuilderUtil.ok(productService.getProductByCode(code));
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.notFound(e.getMessage());
        }
    }

    @GetMapping(
            path = IApplicationConstant.RestVersion.Product.VIEW_ITEM_ID,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResultDto<DetailProductDto>> getProductById(
            @PathVariable(IApplicationConstant.CommonValue.CommonRestPath.ID) @NotNull @Valid Long id
    ) {
        try {
            return ResultBuilderUtil.ok(productService.getProductById(id));
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.notFound(e.getMessage());
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultDto<UpdateProductDto>> addProduct(
            Authentication authentication,
            @RequestPart("data") @Valid AddProductDto detailDto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            return ResultBuilderUtil.ok(productService.addProduct(authentication, detailDto, image));
        } catch (UserNotAllowedException e) {
            return ResultBuilderUtil.unauthorized();
        } catch (UploadImageException e) {
            return ResultBuilderUtil.badRequest(e.getMessage());
        }
    }

    @PutMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResultDto<UpdateProductDto>> updateProduct(
            Authentication authentication,
            @RequestPart("data") @Valid UpdateProductDto detailDto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            return ResultBuilderUtil.ok(productService.updateProduct(authentication, detailDto, image));
        } catch (UserNotAllowedException e) {
            return ResultBuilderUtil.unauthorized();
        } catch (UploadImageException e) {
            return ResultBuilderUtil.badRequest(e.getMessage());
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.notFound(e.getMessage());
        }
    }

    @PatchMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResultDto<UpdateProductDto>> addProductStock(
            Authentication authentication, @RequestBody @Valid RequestAddStockDto addStockDto
    ) {
        try {
            return ResultBuilderUtil.ok(productService.addStock(authentication, addStockDto));
        } catch (UserNotAllowedException e) {
            return ResultBuilderUtil.unauthorized();
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.notFound(e.getMessage());
        }
    }

    @DeleteMapping(
            path = IApplicationConstant.RestVersion.Product.VIEW_ITEM_ID,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDto<Boolean>> deleteProduct(
            Authentication authentication, @PathVariable(value = IApplicationConstant.CommonValue.CommonRestPath.ID) Long id
    ) {
        try {
            productService.deleteProductByEditingIsDelete(authentication, id);
            return ResultBuilderUtil.ok(true);
        } catch (UserNotAllowedException e) {
            return ResultBuilderUtil.unauthorized();
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.notFound(false, e.getMessage());
        }
    }

}
