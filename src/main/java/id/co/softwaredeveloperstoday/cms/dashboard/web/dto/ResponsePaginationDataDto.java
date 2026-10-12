package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ESortType;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EUserSortBy;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class ResponsePaginationDataDto {

    private ResponseSortDataDto<String> sortType;
    private ResponseSortDataDto<ResponseCommonEnumDto> sortBy;
    private List<ResponseCommonEnumDto> searchDataParameters;

    public ResponsePaginationDataDto() {
        this.sortBy = new ResponseSortDataDto<>(
                IApplicationConstant.CommonValue.Pagination.SORT_BY_PARAMETER,
                Stream.of(EUserSortBy.values()).map(
                        b -> new ResponseCommonEnumDto(b.toString(), b.getName())).collect(Collectors.toList())
        );
        this.sortType = new ResponseSortDataDto<>(
                IApplicationConstant.CommonValue.Pagination.SORT_TYPE_PARAMETER,
                Stream.of(ESortType.values()).map(String::valueOf).collect(Collectors.toList())
        );
        this.searchDataParameters = Arrays.stream(EUserSortBy.values()).map(
                b -> new ResponseCommonEnumDto(b.toString(), b.getName())).collect(Collectors.toList()
        );
    }
}
