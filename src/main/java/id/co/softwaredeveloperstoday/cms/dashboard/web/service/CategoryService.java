package id.co.softwaredeveloperstoday.cms.dashboard.web.service;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.CategoryDataTableDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.CategoryDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.ResponseDataTableDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAll();

    ResponseDataTableDto<CategoryDataTableDto> getAll(
            Authentication authentication, int draw, String search, Integer page, Integer size
    );

    CategoryDto patchCategory(Authentication authentication, CategoryDto updateCategoryDto);

    void deleteCategoryByEditingIsDelete(Authentication authentication, Long id);

}
