package id.co.softwaredeveloperstoday.cms.dashboard.web.mapper;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.Category;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.Product;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.common.ServiceUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;
import java.util.Objects;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    public abstract CategoryDto convertCategory(Category category);

    @Mapping(target = "categoryDto", expression = "java(convertCategory(product.getCategory()))")
    public abstract ProductDataTableDto convertDataTableDto(Product product);

    @Mapping(target = "categoryDto", expression = "java(convertCategory(product.getCategory()))")
    public abstract UpdateProductDto convertUpdateProductDto(Product product);

    public abstract Category convertCategory(CategoryDto categoryDto);

    @Mapping(target = "category", expression = "java(convertCategory(addProductDto.getCategoryDto()))")
    public abstract Product convertAddedProductDto(AddProductDto addProductDto);

    @Mapping(target = "category", expression = "java(convertCategory(updateProductDto.getCategoryDto()))")
    public abstract Product convertUpdateProductDto(UpdateProductDto updateProductDto);

    @Mapping(target = "categoryDto", expression = "java(convertCategory(product.getCategory()))")
    @Mapping(target = "encodedImage", expression = "java(encodeImage(product.getImage()))")
    public abstract DetailProductDto convertDetailProductDto(Product product);

    @Mapping(target = "productId", source = "id")
    @Mapping(target = "productName", source = "name")
    @Mapping(target = "currentPricePerItemUnit", source = "pricePerUnit")
    @Mapping(target = "currentPricePerItemUnitWithCurrency", expression = "java(convertPriceWithCurrency(product.getPricePerUnit()))")
    public abstract CalculateOrderItemDto convertCalculateOrderItemDto(Product product);

    @Mapping(target = "categoryDto", expression = "java(convertCategory(product.getCategory()))")
    @Mapping(target = "currencyPricePerUnit", expression = "java(convertPriceWithCurrency(product.getPricePerUnit()))")
    public abstract ProductOrderDataTableDto convertProductOrderDataTableDto(Product product);

    public static String convertPriceWithCurrency(Double price) {
        return ServiceUtil.convertPriceWithCurrency(price);
    }

    public static String encodeImage(byte[] image) {
        if (Objects.nonNull(image))
            return Base64.getEncoder().encodeToString(image);

        return null;
    }

}
