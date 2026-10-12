package id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result;

import java.util.List;

public interface ResultListDto<T> extends ResponseResultDataDto  {

    List<T> getData();

    Long getTotalData();

}
