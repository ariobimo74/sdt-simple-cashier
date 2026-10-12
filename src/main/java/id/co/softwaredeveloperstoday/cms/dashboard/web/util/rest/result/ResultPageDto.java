package id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result;

public interface ResultPageDto<T> extends ResultListDto<T> {

    Integer getTotalPage();

}
