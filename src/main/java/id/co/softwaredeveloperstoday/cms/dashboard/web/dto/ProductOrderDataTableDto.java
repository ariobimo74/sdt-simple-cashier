package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrderDataTableDto extends ProductDataTableDto {

    private byte[] image;
    private Double pricePerUnit;
    private String currencyPricePerUnit;

}
