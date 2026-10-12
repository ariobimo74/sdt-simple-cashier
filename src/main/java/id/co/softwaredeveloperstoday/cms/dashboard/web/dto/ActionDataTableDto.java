package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EDataTableAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ActionDataTableDto {

    private String htmlClass;
    private String title;
    private String icon;

    public  ActionDataTableDto(EDataTableAction action) {
        this.htmlClass = action.getHtmlClass();
        this.title = action.getTitle();
        this.icon = action.getIcon();
    }
}
