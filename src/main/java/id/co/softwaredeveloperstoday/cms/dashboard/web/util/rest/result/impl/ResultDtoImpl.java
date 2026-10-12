package id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.impl;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.dto.ResponseDataDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultDtoImpl<T> extends BaseResponseDataDto implements ResultDto<T> {

    private T data;

    public ResultDtoImpl(ResponseDataDto responseData) {
        super(responseData);
    }
}
