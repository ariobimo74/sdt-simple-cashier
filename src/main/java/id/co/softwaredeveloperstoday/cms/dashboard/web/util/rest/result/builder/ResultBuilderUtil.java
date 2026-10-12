package id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.builder;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultListDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultPageDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.impl.ResultDtoImpl;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.impl.ResultListDtoImpl;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.impl.ResultPageDtoImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

public class ResultBuilderUtil extends BaseResponseDataBuilder {

    public static <T> ResponseEntity<ResultDto<T>> ok(T data) {
        ResultDtoImpl<T> resultDto = new ResultDtoImpl<>();
        resultDto.setData(data);
        resultDto.setResponseData(responseDataOk());

        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResultListDto<T>> ok(List<T> data) {
        ResultListDtoImpl<T> resultListDto = new ResultListDtoImpl<>();
        resultListDto.setTotalData((long) data.size());
        resultListDto.setData(data);
        resultListDto.setResponseData(responseDataOk());

        return new ResponseEntity<>(resultListDto, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResultPageDto<T>> ok(Page<T> data) {
        ResultPageDtoImpl<T> resultPageDto = new ResultPageDtoImpl<>();
        resultPageDto.setTotalData(data.getTotalElements());
        resultPageDto.setTotalPage(data.getTotalPages());
        resultPageDto.setData(data.getContent());
        resultPageDto.setResponseData(responseDataOk());

        return new ResponseEntity<>(resultPageDto, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResultDto<T>> internalServerError(T data, String errorMessage) {
        ResultDtoImpl<T> resultDto = new ResultDtoImpl<>();
        resultDto.setData(data);
        resultDto.setResponseData(responseDataInternalServerError(errorMessage));

        return new ResponseEntity<>(resultDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ResponseEntity<ResultDto<T>> badRequest(T data, String errorMessage) {
        ResultDtoImpl<T> resultDto = new ResultDtoImpl<>();
        resultDto.setData(data);
        resultDto.setResponseData(responseDataBadRequest(errorMessage));

        return new ResponseEntity<>(resultDto, HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<ResultDto<T>> notFound(T data, String errorMessage) {
        ResultDtoImpl<T> resultDto = new ResultDtoImpl<>();
        resultDto.setData(data);
        resultDto.setResponseData(responseDataNotFound(errorMessage));

        return new ResponseEntity<>(resultDto, HttpStatus.NOT_FOUND);
    }

    public static <T> ResponseEntity<ResultDto<T>> unauthorized() {
        ResultDtoImpl<T> resultDto = new ResultDtoImpl<>();
        resultDto.setData(null);
        resultDto.setResponseData(responseDataUnauthorized(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_UNAUTHORIZED));

        return new ResponseEntity<>(resultDto, HttpStatus.UNAUTHORIZED);
    }

    public static <T> ResponseEntity<ResultDto<T>> internalServerError(String errorMessage) {
        return internalServerError(null, errorMessage);
    }

    public static <T> ResponseEntity<ResultDto<T>> badRequest(String errorMessage) {
        ResultDtoImpl<String> resultDto = new ResultDtoImpl<>();
        resultDto.setData(errorMessage);
        resultDto.setResponseData(responseDataBadRequest(errorMessage));

        return badRequest(null, errorMessage);
    }

    public static <T> ResponseEntity<ResultDto<T>> notFound(String errorMessage) {
        ResultDtoImpl<String> resultDto = new ResultDtoImpl<>();
        resultDto.setData(errorMessage);
        resultDto.setResponseData(responseDataNotFound(errorMessage));

        return notFound(null, errorMessage);
    }

    public static <T> ResponseEntity<ResultListDto<T>> noContent() {
        ResultListDtoImpl<T> resultListDto = new ResultListDtoImpl<>();
        resultListDto.setData(Collections.emptyList());
        resultListDto.setResponseData(responseDataOk());

        return new ResponseEntity<>(resultListDto, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResultPageDto<T>> noPageContent() {
        ResultPageDtoImpl<T> resultListDto = new ResultPageDtoImpl<>();
        resultListDto.setData(Collections.emptyList());
        resultListDto.setResponseData(responseDataOk());

        return new ResponseEntity<>(resultListDto, HttpStatus.NO_CONTENT);
    }

    public static <T> ResponseEntity<ResultPageDto<T>> unauthorizedPage() {
        ResultPageDtoImpl<T> resultListDto = new ResultPageDtoImpl<>();
        resultListDto.setData(Collections.emptyList());
        resultListDto.setResponseData(responseDataUnauthorized(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_UNAUTHORIZED));

        return new ResponseEntity<>(resultListDto, HttpStatus.UNAUTHORIZED);
    }

}
