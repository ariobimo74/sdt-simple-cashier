package id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EUserSortBy {
    NAME("name"), USERNAME("user.username"), ID_CARD_NUMBER("idCardNumber");

    private final String name;
}
