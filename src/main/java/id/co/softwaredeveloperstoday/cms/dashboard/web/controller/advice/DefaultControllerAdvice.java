package id.co.softwaredeveloperstoday.cms.dashboard.web.controller.advice;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EGenderType;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EMaritalStatus;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EMemberLevel;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleName;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.dto.ResponseDataDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.ResultDto;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.builder.ResultBuilderUtil;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.rest.result.impl.ResultDtoImpl;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class DefaultControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        return new ResponseEntity<>(buildResponse(HttpStatus.BAD_REQUEST.value(), ex.getParameterName() + " is required"),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Invalid JSON Request.";

        if (ex.getLocalizedMessage().contains(EGenderType.class.getPackageName()))
            error += "Please check Gender Type";
        else if (ex.getLocalizedMessage().contains(EMemberLevel.class.getPackageName()))
            error += "Please check Member Type Parameter";
        else if (ex.getLocalizedMessage().contains(ERoleName.class.getPackageName()))
            error += "Please check Role Type Parameter";
        else if (ex.getLocalizedMessage().contains(EMaritalStatus.class.getPackageName()))
            error += "Please check Marital Type Parameter";

        return new ResponseEntity<>(buildResponse(HttpStatus.BAD_REQUEST.value(), error), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                             HttpHeaders headers, HttpStatus status,
                                                             WebRequest request) {
        ex.printStackTrace();
        return new ResponseEntity<>(buildResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "unhandled error. please contact administrator for this"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleNoHandlerFoundException(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (!ex.getBindingResult().getAllErrors().isEmpty() && Objects.nonNull(ex.getBindingResult().getAllErrors().get(0))
                && Objects.nonNull(ex.getBindingResult().getAllErrors().get(0).getCode())
                && Objects.requireNonNull(ex.getBindingResult().getAllErrors().get(0).getCode()).contains(IApplicationConstant.CommonValue.CommonRestParam.PARAM_STRONG_PASSWORD))
            return new ResponseEntity<>(buildResponse(HttpStatus.BAD_REQUEST.value(), IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_STRONG_PASSWORD),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(buildResponse(HttpStatus.BAD_REQUEST.value(), IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_GENERAL_REQUIRED_FIELDS),
                HttpStatus.BAD_REQUEST);
    }

    private ResultDto<Object> buildResponse(int httpStatus, String errorMessage) {
        return new ResultDtoImpl<>(new ResponseDataDto(
                IApplicationConstant.CommonValue.RestResponseValue.FAILED, httpStatus, errorMessage)
        );
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ResultDto<String>> handleFileSizeLimitExceeded(FileSizeLimitExceededException e) {
        return ResultBuilderUtil.badRequest(e.getMessage() + " : actual size = " + e.getActualSize(),
                IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_TRANSACTION_ORDER_UPLOADED_ATTACHMENT_MAX_SIZE);
    }
}
