package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto {

    private String menuName;
    private String menuClass;
    private String iconClass;
    private String url;
    private List<MenuDto> subMenu;
    private int orderNumber;

}
