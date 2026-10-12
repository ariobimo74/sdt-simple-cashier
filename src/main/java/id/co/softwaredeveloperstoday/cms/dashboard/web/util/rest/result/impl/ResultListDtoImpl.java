package id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.impl;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultListDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultListDtoImpl<T> extends BaseResponseDataDto implements ResultListDto<T> {

    private List<T> data;
    private Long totalData;

}
