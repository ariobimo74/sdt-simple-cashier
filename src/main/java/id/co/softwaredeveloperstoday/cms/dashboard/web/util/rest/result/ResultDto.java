package id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result;

public interface ResultDto<T> extends ResponseResultDataDto {

    T getData();

}
