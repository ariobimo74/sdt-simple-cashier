package id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.impl;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultPageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultPageDtoImpl<T> extends ResultListDtoImpl<T> implements ResultPageDto<T> {

    private Integer totalPage;

}
