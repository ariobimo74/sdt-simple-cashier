package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDataTableDto extends CategoryDto {

    private ActionDataTableIdentityDto actionsId;

    public CategoryDataTableDto(Long id, String name) {
        super(id, name);
    }
}
