package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculateOrderItemDto {

    private Long productId;
    private String productName;
    private Double currentPricePerItemUnit;
    private Integer totalItem;
    private Double sumTotalAllItemPrice;
    private byte[] image;

    private String currentPricePerItemUnitWithCurrency;
    private String sumTotalAllItemPriceWithCurrency;

}
