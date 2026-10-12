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
public class ActionDataTableIdentityDto {

    private Long id;
    private List<ActionDataTableDto> actions;

    public ActionDataTableIdentityDto(Long id) {
        this.id = id;
    }
}
