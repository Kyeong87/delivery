package com.project.delivery.config.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import javax.validation.ValidationException;
import java.nio.charset.Charset;

@ControllerAdvice
public class ApiExceptionHandler {

    private Logger log = LogManager.getLogger(this.getClass());

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ApiExceptionInfo> apiException(ApiException e) {
        log.error("API Exception : {}", e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error("{}", element.toString());
        }
        final ApiExceptionInfo response = new ApiExceptionInfo();
        response.setHttpStatus(e.getStatusCode());
        response.setMessage(e.getMessage());
        response.setSuccess(false);
        return new ResponseEntity<>(response, e.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiExceptionInfo> methodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error("API Exception : {}", e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error("{}", element.toString());
        }
        final ApiExceptionInfo response = new ApiExceptionInfo();
        response.setHttpStatus(HttpStatus.BAD_REQUEST);
        response.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        response.setSuccess(false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiExceptionInfo> exception(Exception e) {
        log.error("API Exception : {}", e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error("{}", element.toString());
        }
        final ApiExceptionInfo response = new ApiExceptionInfo();
        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setMessage(e.getMessage());
        response.setSuccess(false);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<ApiExceptionInfo> validationException(ValidationException e) {
        log.error("API Exception : {}", e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error("{}", element.toString());
        }
        final ApiExceptionInfo response = new ApiExceptionInfo();
        response.setHttpStatus(HttpStatus.BAD_REQUEST);
        String message = e.getMessage();
        String[] splited= message.split(": ");
        String pureMessage = splited[splited.length - 1];
        response.setMessage(pureMessage);
        response.setSuccess(false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiExceptionInfo> methodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        log.error("API Exception : {}", e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error("{}", element.toString());
        }
        final ApiExceptionInfo response = new ApiExceptionInfo();
        response.setHttpStatus(HttpStatus.BAD_REQUEST);
        response.setMessage(e.getMessage());
        response.setSuccess(false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ApiExceptionInfo> bindException(BindException e) {
        for (StackTraceElement element : e.getStackTrace()) {
            log.error("{}", element.toString());
        }
        final ApiExceptionInfo response = new ApiExceptionInfo();
        response.setHttpStatus(HttpStatus.BAD_REQUEST);
        response.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        response.setSuccess(false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WebClientResponseException.class)
    protected ResponseEntity<ApiExceptionInfo> webClientResponseException(WebClientResponseException e) throws JsonProcessingException {

        for (StackTraceElement element : e.getStackTrace()) {
            log.error("webClientResponseException {}", element.toString());
        }

        if(StringUtils.isEmpty(e.getResponseBodyAsString())) {

            ApiExceptionInfo response = new ApiExceptionInfo();
            response.setHttpStatus(e.getStatusCode());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            response.setErrorCode(e.getStatusText());

            return new ResponseEntity<>(response, e.getStatusCode());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ApiExceptionInfo response = objectMapper.readValue(e.getResponseBodyAsString(Charset.defaultCharset()), ApiExceptionInfo.class);

        return new ResponseEntity<>(response, e.getStatusCode());
    }
}
