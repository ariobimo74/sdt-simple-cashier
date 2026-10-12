package id.co.softwaredeveloperstoday.cms.dashboard.web.service;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EPdfTemplatePath;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ETransactionHistorySortBy;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OrderService {

    ResponseDataTableDto<ProductOrderDataTableDto> getAllPagingDataTable(
            int draw, String search, Integer page, Integer size
    );

    OrderDetailDto getOrderById(Long id);

    CalculateOrderDto calculateOrder(List<RequestCalculateOrderDto> requestCalculateOrderDtoList);

    OrderDetailDto checkout(List<RequestCalculateOrderDto> requestCalculateOrderDtoList);

    OrderDetailDto payment(RequestPaymentDto requestPaymentDto, MultipartFile image);

    ResponseDataTableDto<TransactionHistoryDataTableDto> getAllTransactionHistoryDataTable(
            int draw, String search, Integer page, Integer size, ETransactionHistorySortBy sortBy, boolean isAscendingSort
    );

    ResponseGeneratedPdfDto generateAndDownloadPdf(Long orderId, EPdfTemplatePath path) throws IOException;

}
