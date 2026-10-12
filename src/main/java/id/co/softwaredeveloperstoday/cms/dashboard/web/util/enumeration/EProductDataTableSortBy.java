package id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EProductDataTableSortBy {

    NAME("name"), CATEGORY("category.name"), CODE("code"), STOCK("stock");

    private final String name;

}
