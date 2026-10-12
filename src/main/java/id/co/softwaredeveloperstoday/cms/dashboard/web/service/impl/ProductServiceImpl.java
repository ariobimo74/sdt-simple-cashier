package id.co.softwaredeveloperstoday.cms.dashboard.web.service.impl;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dao.ProductDao;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.factory.AuthorizationRoleLevelFactory;
import id.co.softwaredeveloperstoday.cms.dashboard.web.mapper.ProductMapper;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.Category;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.Product;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.AuthorizationRoleLevelService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.AuthorizeRoleService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.ProductService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.common.PageableDataTableCommonUtil;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EDataTableAction;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EProductDataTableSortBy;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleLevel;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.DataNotFoundException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.UploadImageException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;

    private final ProductMapper productMapper;

    private final AuthorizeRoleService authorizeRoleService;

    private final AuthorizationRoleLevelFactory roleLevelFactory;

    @Override
    public ResponseDataTableDto<ProductDataTableDto> getAllPagingDataTable(
            Authentication authentication, int draw, String search, Integer page, Integer size, EProductDataTableSortBy sortBy, boolean isAscendingSort
    ) {
        Page<Product> products;

        if (StringUtils.isBlank(search))
            products = productDao.findAll(PageableDataTableCommonUtil.pageable(page, size, sortBy.getName(), isAscendingSort));
        else products = productDao.findAll(
                specificationProductDataTable(search), PageableDataTableCommonUtil.pageable(page, size, sortBy.getName(), isAscendingSort)
        );

        if (products.getTotalElements() == 0)
            return new ResponseDataTableDto<>(draw, 0, 0, new ArrayList<>());
        else return new ResponseDataTableDto<>(draw, productDao.count(), products.getTotalElements(),
                products.getContent().stream().map(productMapper::convertDataTableDto).peek(
                        dt -> dt.setActionsId(determineDataTableActions(dt.getId(), authentication))
                ).collect(Collectors.toList()));
    }

    @Override
    public DetailProductDto getProductById(Long id) {
        Product product = productDao.findById(id).orElseThrow(() -> new DataNotFoundException(
                IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND)
        );

        return productMapper.convertDetailProductDto(product);
    }

    @Override
    public UpdateProductDto addProduct(Authentication authentication, AddProductDto addProductDto, MultipartFile image) {
        authorizeRoleService.authorizeRegularAdmin(authentication);

        if (Objects.nonNull(image)) {
            try {
                addProductDto.setImage(image.getBytes());
            } catch (IOException e) {
                throw new UploadImageException();
            }
        }

        Product product = productMapper.convertAddedProductDto(addProductDto);
        product.setCreatedDate(new Date());
        product.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        return productMapper.convertUpdateProductDto(productDao.save(product));
    }

    @Override
    public UpdateProductDto updateProduct(Authentication authentication, UpdateProductDto updateProductDto, MultipartFile image) {
        authorizeRoleService.authorizeRegularAdmin(authentication);

        if (Objects.nonNull(image)) {
            try {
                updateProductDto.setImage(image.getBytes());
            } catch (IOException e) {
                throw new UploadImageException();
            }
        }

        Product referencedProduct = productDao.findById(updateProductDto.getId()).orElseThrow(() -> new DataNotFoundException(
                IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND)
        );
        Product newProduct = productMapper.convertUpdateProductDto(updateProductDto);
        newProduct.setCreatedBy(referencedProduct.getCreatedBy());
        newProduct.setCreatedDate(referencedProduct.getCreatedDate());
        newProduct.setModifiedDate(new Date());
        newProduct.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        newProduct.setIsDelete(referencedProduct.getIsDelete());

        return productMapper.convertUpdateProductDto(productDao.save(newProduct));
    }

    @Override
    public UpdateProductDto addStock(Authentication authentication, RequestAddStockDto addStockDto) {
        authorizeRoleService.authorizeRegularAdmin(authentication);

        Product product = productDao.findById(addStockDto.getId()).orElseThrow(() -> new DataNotFoundException(
                IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND)
        );
        product.setStock(product.getStock() + addStockDto.getAddedStock());

        return productMapper.convertUpdateProductDto(productDao.save(product));
    }

    @Override
    public UpdateProductDto getProductByCode(String code) {
        Product product = productDao.findFirstByCode(code).orElseThrow(() -> new DataNotFoundException(
                IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND)
        );

        return productMapper.convertUpdateProductDto(product);
    }

    @Override
    public void deleteProductByEditingIsDelete(Authentication authentication, Long id) {
        authorizeRoleService.authorizeRegularAdmin(authentication);
        Product product = productDao.findById(id).orElseThrow(
                () -> new DataNotFoundException(
                        IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND
        ));
        product.setIsDelete(true);
        product.setModifiedDate(new Date());
        product.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        productDao.save(product);
    }

    static Specification<Product> specificationProductDataTable(String search) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT);

            return criteriaBuilder.or(
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, root.get("createdDate"), criteriaBuilder.literal("YYYY-MM-DD")), '%' + search + '%'),
                            criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, root.get("modifiedDate"), criteriaBuilder.literal("YYYY-MM-DD")), '%' + search + '%')
                    ),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), '%' + search.toLowerCase() + '%'),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), '%' + search.toLowerCase() + '%'),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("stock").as(String.class)), '%' + search.toLowerCase() + '%'),
                    criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("name")), '%' + search.toLowerCase() + '%')
            );
        };
    }

    public ActionDataTableIdentityDto determineDataTableActions(Long id, Authentication authentication) {
        ActionDataTableIdentityDto result = new ActionDataTableIdentityDto(id);
        List<ActionDataTableDto> identityDtoList = new ArrayList<>(List.of(new ActionDataTableDto(EDataTableAction.DETAIL)));
        AuthorizationRoleLevelService roleLevelService = roleLevelFactory.determineService(ERoleLevel.REGULAR_ADMIN);
        if (roleLevelService.isAuthorized(authentication)) {
            identityDtoList.add(new ActionDataTableDto(EDataTableAction.UPDATE));
            identityDtoList.add(new ActionDataTableDto(EDataTableAction.DELETE));
        }
        result.setActions(identityDtoList);

        return result;
    }

}
