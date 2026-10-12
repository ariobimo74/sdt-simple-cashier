package id.co.softwaredeveloperstoday.cms.dashboard.web.endpoint;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.OrderService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EPdfTemplatePath;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ESortType;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ETransactionHistorySortBy;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.DataNotFoundException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.InvalidDataException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.builder.ResultBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping(
        value = IApplicationConstant.RestVersion.Order.ORDER_V1
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class OrderEndpoint {

    private final OrderService orderService;

    @GetMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseDataTableDto<ProductOrderDataTableDto>> getProductOrderDataTable(
            @RequestParam int draw,
            @RequestParam int start,
            @RequestParam int length,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, value = "order[0][column]", defaultValue = "0") int orderColumn,
            @RequestParam(required = false, value = "order[0][dir]", defaultValue = "") String orderDir
    ) {
        return new ResponseEntity<>(orderService.getAllPagingDataTable(draw, search, (start / length) + 1, length),
                HttpStatus.OK);
    }

    @GetMapping(
            path = IApplicationConstant.RestVersion.Order.DETAIL_ID,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResultDto<OrderDetailDto>> getOrderById(
            @PathVariable(IApplicationConstant.CommonValue.CommonRestPath.ID) @NotNull @Valid Long id
    ) {
        try {
            return ResultBuilderUtil.ok(orderService.getOrderById(id));
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.badRequest(e.getMessage());
        }
    }

    @PostMapping(
            path = IApplicationConstant.RestVersion.Order.CALCULATE_ORDER,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResultDto<CalculateOrderDto>> calculateOrder(
            @RequestBody(required = false) List<RequestCalculateOrderDto> requestCalculateOrderDtoList
    ) {
        try {
            return ResultBuilderUtil.ok(orderService.calculateOrder(requestCalculateOrderDtoList));
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.ok(new CalculateOrderDto(0D, 0, new ArrayList<>(), "Rp. 0"));
        }
    }

    @PostMapping(
            path = IApplicationConstant.RestVersion.Order.CHECKOUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResultDto<OrderDetailDto>> checkout(@RequestBody(required = false) List<RequestCalculateOrderDto> requestCalculateOrderDtoList) {
        try {
            return ResultBuilderUtil.ok(orderService.checkout(requestCalculateOrderDtoList));
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.notFound(e.getMessage());
        }
    }

    @PatchMapping(
            path = IApplicationConstant.RestVersion.Order.PAYMENT,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResultDto<OrderDetailDto>> payment(
            @RequestPart("data") @Valid RequestPaymentDto requestPaymentDto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            return ResultBuilderUtil.ok(orderService.payment(requestPaymentDto, image));
        } catch (DataNotFoundException e) {
            return ResultBuilderUtil.notFound(e.getMessage());
        } catch (InvalidDataException e) {
            return ResultBuilderUtil.badRequest(e.getMessage());
        }
    }

    @GetMapping(
            path = IApplicationConstant.RestVersion.Order.HISTORY,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseDataTableDto<TransactionHistoryDataTableDto>> getTransactionHistoryDataTable(
            @RequestParam int draw,
            @RequestParam int start,
            @RequestParam int length,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, value = "order[0][column]", defaultValue = "0") int orderColumn,
            @RequestParam(required = false, value = "order[0][dir]", defaultValue = "") String orderDir
    ) {
        return new ResponseEntity<>(orderService.getAllTransactionHistoryDataTable(draw, search, (start / length) + 1, length,
                Stream.of(ETransactionHistorySortBy.values()).filter(s -> s.ordinal() == orderColumn - 1).findFirst()
                        .orElse(ETransactionHistorySortBy.CREATED_DATE), orderDir.equalsIgnoreCase(ESortType.ASC.toString())),
                HttpStatus.OK);
    }

    @GetMapping(
            path = IApplicationConstant.RestVersion.Order.DOWNLOAD_PDF_ID
    )
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable(IApplicationConstant.CommonValue.CommonRestPath.ID) @NotNull @Valid Long id
    ) {
        try {
            ResponseGeneratedPdfDto generatedPdfDto = orderService.generateAndDownloadPdf(id, EPdfTemplatePath.PRINT);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + generatedPdfDto.getOrderNumber() + "_" + generatedPdfDto.getGeneratedDate() + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(generatedPdfDto.getPdfFile());
        } catch (DataNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
