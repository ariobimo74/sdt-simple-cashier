package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculateOrderDto {

    private Double totalPrice;
    private Integer totalOrderedItem;
    private List<CalculateOrderItemDto> orderItemDtoList;

    private String totalPriceWithCurrency;

}
