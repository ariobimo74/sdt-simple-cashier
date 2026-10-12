package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaleDto {

    private Long id;
    private String name;
    private Integer totalItem;

}
