package id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EPdfTemplatePath {

    PRINT("invoice/print"), MAIL("invoice/mail");

    private final String name;

}
