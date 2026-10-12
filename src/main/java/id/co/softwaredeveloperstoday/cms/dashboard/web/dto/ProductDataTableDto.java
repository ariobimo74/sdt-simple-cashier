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
public class ProductDataTableDto {

    private Long id;
    private String name;
    private CategoryDto categoryDto;
    private String code;
    private Long stock;
    private ActionDataTableIdentityDto actionsId;

}
