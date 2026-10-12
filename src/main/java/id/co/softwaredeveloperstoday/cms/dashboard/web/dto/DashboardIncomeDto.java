package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardIncomeDto {

    private String userName;
    private String totalSalesLast6Month;
    private String totalSalesToday;
    private String currentUserSalesToday;
    private List<ProductSaleDto> todaySaleProducts;
    Integer totalTodayProductSale;
    private List<ProductSaleDto> monthlySaleProducts;
    Integer totalProductSaleInAMounth;

}
