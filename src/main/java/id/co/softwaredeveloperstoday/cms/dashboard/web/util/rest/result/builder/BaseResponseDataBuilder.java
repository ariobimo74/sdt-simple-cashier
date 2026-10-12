package id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.builder;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.dto.ResponseDataDto;
import org.springframework.http.HttpStatus;

public class BaseResponseDataBuilder {

    static ResponseDataDto responseDataOk() {
        return new ResponseDataDto(
                IApplicationConstant.CommonValue.RestResponseValue.SUCCESS, HttpStatus.OK.value(), ""
        );
    }

    static ResponseDataDto responseDataOk(String message) {
        return new ResponseDataDto(
                IApplicationConstant.CommonValue.RestResponseValue.SUCCESS, HttpStatus.OK.value(), message
        );
    }

    static ResponseDataDto responseDataInternalServerError(String message) {
        return new ResponseDataDto(
                IApplicationConstant.CommonValue.RestResponseValue.FAILED, HttpStatus.INTERNAL_SERVER_ERROR.value(), message
        );
    }

    static ResponseDataDto responseDataBadRequest(String message) {
        return new ResponseDataDto(
                IApplicationConstant.CommonValue.RestResponseValue.FAILED, HttpStatus.BAD_REQUEST.value(), message
        );
    }

    static ResponseDataDto responseDataNotFound(String message) {
        return new ResponseDataDto(
                IApplicationConstant.CommonValue.RestResponseValue.FAILED, HttpStatus.NOT_FOUND.value(), message
        );
    }

    static ResponseDataDto responseDataUnauthorized(String message) {
        return new ResponseDataDto(
                IApplicationConstant.CommonValue.RestResponseValue.FAILED, HttpStatus.UNAUTHORIZED.value(), message
        );
    }

}
