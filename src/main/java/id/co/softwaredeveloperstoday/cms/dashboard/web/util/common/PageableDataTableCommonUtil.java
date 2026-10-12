package id.co.softwaredeveloperstoday.cms.dashboard.web.util.common;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class PageableDataTableCommonUtil {

    public static Pageable pageable(Integer page, Integer size) {
        return pageable(page, size, null, false);
    }

    public static Pageable pageable(Integer page, Integer size, String sortBy, boolean isAscendingSort) {
        Sort sort = Sort.unsorted();
        if (StringUtils.isNotBlank(sortBy)) {
            sort = Sort.by(sortBy);
            if (isAscendingSort) sort = sort.ascending();
            else sort = sort.descending();
        }

        return PageRequest.of(
                Optional.ofNullable(page).orElse(IApplicationConstant.CommonValue.Pagination.DEFAULT_PAGE_NUMBER) -
                        IApplicationConstant.CommonValue.Pagination.DEFAULT_PAGE_NUMBER,
                Optional.ofNullable(size).orElse(IApplicationConstant.CommonValue.Pagination.DEFAULT_PAGE_SIZE), sort
        );
    }

}
