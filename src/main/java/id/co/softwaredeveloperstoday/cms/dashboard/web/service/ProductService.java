package id.co.softwaredeveloperstoday.cms.dashboard.web.service;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EProductDataTableSortBy;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ResponseDataTableDto<ProductDataTableDto> getAllPagingDataTable(
            Authentication authentication, int draw, String search, Integer page, Integer size, EProductDataTableSortBy sortBy, boolean isAscendingSort
    );

    DetailProductDto getProductById(Long id);

    UpdateProductDto addProduct(Authentication authentication, AddProductDto addProductDto, MultipartFile image);

    UpdateProductDto updateProduct(Authentication authentication, UpdateProductDto updateProductDto, MultipartFile image);

    UpdateProductDto addStock(Authentication authentication, RequestAddStockDto addStockDto);

    UpdateProductDto getProductByCode(String code);

    void deleteProductByEditingIsDelete(Authentication authentication, Long id);

}
