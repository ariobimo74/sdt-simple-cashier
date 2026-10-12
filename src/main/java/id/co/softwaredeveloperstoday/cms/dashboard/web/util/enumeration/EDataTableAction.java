package id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration;

public enum EDataTableAction {
    DETAIL("btn btn-primary detail-item-btn", "View Detail", "&#xf15c"),
    UPDATE("btn btn-warning edit-item-btn", "Edit Data", "&#xf044"),
    DELETE("btn btn-danger delete-item-btn", "Delete Data", "&#xf1f8");

    private String htmlClass;
    private String title;
    private String icon;

    EDataTableAction(String htmlClass, String title, String icon) {
        this.htmlClass = htmlClass;
        this.title = title;
        this.icon = icon;
    }

    public String getHtmlClass() {
        return htmlClass;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }
}
