package id.co.softwaredeveloperstoday.cms.dashboard.web.service.impl;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dao.CategoryDao;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.factory.AuthorizationRoleLevelFactory;
import id.co.softwaredeveloperstoday.cms.dashboard.web.mapper.ProductMapper;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.Category;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.AuthorizationRoleLevelService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.AuthorizeRoleService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.CategoryService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.common.PageableDataTableCommonUtil;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EDataTableAction;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleLevel;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;

    private final ProductMapper productMapper;

    private final AuthorizationRoleLevelFactory roleLevelFactory;

    private final AuthorizeRoleService authorizeRoleService;

    @Override
    public List<CategoryDto> getAll() {
        List<Category> categories = categoryDao.findAllByIsDeleteOrderByNameAsc(false);

        if (categories.isEmpty())
            throw new DataNotFoundException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND);
        else return categories.stream().map(productMapper::convertCategory).collect(Collectors.toList());
    }

    @Override
    public ResponseDataTableDto<CategoryDataTableDto> getAll(Authentication authentication, int draw, String search, Integer page, Integer size) {
        Page<CategoryDataTableDto> categoryPage = categoryDao.findByNameContainingIgnoreCaseOrderByNameAsc(search, PageableDataTableCommonUtil.pageable(page, size))
                .map(c -> new CategoryDataTableDto(c.getId(), c.getName()));

        if (categoryPage.getTotalElements() == 0)
            return new ResponseDataTableDto<>(draw, 0, 0, new ArrayList<>());
        else {
            AuthorizationRoleLevelService roleLevelService = roleLevelFactory.determineService(ERoleLevel.REGULAR_ADMIN);
            return new ResponseDataTableDto<>(draw, categoryDao.count(), categoryPage.getTotalElements(), categoryPage.getContent().stream().peek(c -> {
                if (roleLevelService.isAuthorized(authentication))
                    c.setActionsId(new ActionDataTableIdentityDto(c.getId(), List.of(
                            new ActionDataTableDto(EDataTableAction.UPDATE), new ActionDataTableDto(EDataTableAction.DELETE)))
                    );
            }).collect(Collectors.toList()));
        }
    }

    @Override
    public CategoryDto patchCategory(Authentication authentication, CategoryDto updateCategoryDto) {
        Date today = new Date();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        authorizeRoleService.authorizeRegularAdmin(authentication);
        Category category = new Category();

        if (Objects.equals(updateCategoryDto.getId(), 0L)) {
            category.setCreatedDate(today);
            category.setCreatedBy(username);
        } else category = categoryDao.findById(updateCategoryDto.getId()).orElseThrow(
                () -> new DataNotFoundException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND)
        );

        category.setName(updateCategoryDto.getName());
        category.setModifiedDate(today);
        category.setModifiedBy(username);

        return productMapper.convertCategory(categoryDao.save(category));
    }

    @Override
    public void deleteCategoryByEditingIsDelete(Authentication authentication, Long id) {
        authorizeRoleService.authorizeRegularAdmin(authentication);
        Category category = categoryDao.findById(id).orElseThrow(
                () -> new DataNotFoundException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND)
        );

        category.setIsDelete(true);
        category.setModifiedDate(new Date());
        category.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        categoryDao.save(category);
    }

}
