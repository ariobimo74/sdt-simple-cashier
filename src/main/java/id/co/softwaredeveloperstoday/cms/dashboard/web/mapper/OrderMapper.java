package id.co.softwaredeveloperstoday.cms.dashboard.web.mapper;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.CalculateOrderItemDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.OrderDetailDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.ProductSaleDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.TransactionHistoryDataTableDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.Order;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.OrderItem;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EOrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {

    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "currentItemPrice", source = "currentPricePerItemUnit")
    public abstract OrderItem convertOrderItem(CalculateOrderItemDto orderItemDto);

    @Mapping(target = "encodedAttachment", expression = "java(ProductMapper.encodeImage(order.getAttachment()))")
    @Mapping(target = "orderItemDtoList", expression = "java(convertCalculateOrderItemDto(order.getOrderItems()))")
    @Mapping(target = "totalPriceWithCurrency", expression = "java(ProductMapper.convertPriceWithCurrency(order.getTotalPrice()))")
    @Mapping(target = "changeWithCurrency", expression = "java(ProductMapper.convertPriceWithCurrency(order.getChange()))")
    @Mapping(target = "cashPaidByCustomerWithCurrency", expression = "java(ProductMapper.convertPriceWithCurrency(order.getCashPaidByCustomer()))")
    public abstract OrderDetailDto convertOrderDetailDto(Order order);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "image", source = "product.image")
    @Mapping(target = "currentPricePerItemUnit", source = "currentItemPrice")
    @Mapping(target = "currentPricePerItemUnitWithCurrency", expression = "java(ProductMapper.convertPriceWithCurrency(orderItem.getCurrentItemPrice()))")
    @Mapping(target = "sumTotalAllItemPrice", expression = "java(calculatePricePerProduct(orderItem.getCurrentItemPrice(), orderItem.getTotalItem()))")
    @Mapping(target = "sumTotalAllItemPriceWithCurrency", expression = "java(calculatePricePerProductWithCurrency(orderItem.getCurrentItemPrice(), orderItem.getTotalItem()))")
    public abstract CalculateOrderItemDto convertCalculateOrderItemDto(OrderItem orderItem);

    @Mapping(target = "htmlStatus", expression = "java(statusInHtml(order.getStatus()))")
    public abstract TransactionHistoryDataTableDto convertTransactionHistoryDataTableDto(Order order);

    public abstract ProductSaleDto convertProductSale(OrderItem item);

    public List<CalculateOrderItemDto> convertCalculateOrderItemDto(List<OrderItem> orderItems) {
        List<CalculateOrderItemDto> result = new ArrayList<>();
        orderItems.forEach(oi -> {
            CalculateOrderItemDto itemDto = convertCalculateOrderItemDto(oi);
            result.add(itemDto);
        });
        return result;
    }

    public Double calculatePricePerProduct(Double currentItemPrice, Integer totalItem) {
        return currentItemPrice * totalItem;
    }

    public String calculatePricePerProductWithCurrency(Double currentItemPrice, Integer totalItem) {
        return ProductMapper.convertPriceWithCurrency(calculatePricePerProduct(currentItemPrice, totalItem));
    }

    public String statusInHtml(EOrderStatus status) {
        String successStatusButton = "btn btn-success";
        if (Objects.equals(EOrderStatus.UNPAID, status)) successStatusButton = "btn btn-warning";

        return "<button type=\"button\" class=\"" + successStatusButton + "\">" + status + "</button>";
    }

}
