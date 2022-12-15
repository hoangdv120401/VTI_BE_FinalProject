package com.vti.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vti.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private MessageSource messageSource;

    // xu ly exception trong project
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ResponseError> handleCommonException(
            ICommonException ex,
            WebRequest webRequest) {
        ResponseError responseError = new ResponseError()
                .rpCode(ex.getMessageError().getCode())
                .rpStatus("400")
                .rpMessage(httpUtils
                        .populateMessage(
                                ex,
                                new Locale(httpUtils.getLanguage(webRequest)))
                        .getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseError);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseError responseError = new ResponseError()
                .rpCode("NoHandlerFoundException")
                .rpStatus(status.toString())
                .rpMessage(messageSource
                        .getMessage("NoHandlerFoundException.message",
                                null,
                                "",
                                new Locale(httpUtils.getLanguage(request))))
                .detailMessage(exception.getLocalizedMessage());
        return ResponseEntity
                .status(status)
                .body(responseError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseError responseError = new ResponseError()
                .rpCode("HttpRequestMethodNotSupportedException")
                .rpStatus(status.toString())
                .rpMessage(getMessageFromHttpRequestMethodNotSupportedException(exception, request))
                .detailMessage(exception.getLocalizedMessage());
        return ResponseEntity
                .status(status)
                .body(responseError);
    }

    private String getMessageFromHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception, WebRequest webRequest) {
        String message = exception.getMethod() +
                " " +
                messageSource
                        .getMessage("HttpRequestMethodNotSupportedException.message",
                                null,
                                "",
                                new Locale(httpUtils.getLanguage(webRequest)));
        for (HttpMethod method : exception.getSupportedHttpMethods()) {
            message += method + " ";
        }
        return message;
    }

    // Not support media type
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ResponseError responseError = new ResponseError()
                .rpCode("HttpMediaTypeNotSupportedException")
                .rpStatus(status.toString())
                .rpMessage(getMessageFromHttpMediaTypeNotSupportedException(exception, request))
                .detailMessage(exception.getLocalizedMessage());
        return ResponseEntity
                .status(status)
                .body(responseError);

    }

    private String getMessageFromHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException exception,
            WebRequest webRequest) {
        String message = exception.getContentType() +
                " " +
                messageSource
                        .getMessage("HttpMediaTypeNotSupportedException.message",
                                null,
                                "",
                                new Locale(httpUtils.getLanguage(webRequest)));
//                getMessage("HttpMediaTypeNotSupportedException.message");
        for (MediaType method : exception.getSupportedMediaTypes()) {
            message += method + ", ";
        }
        return message.substring(0, message.length() - 2);
    }

    // BindException: This exception is thrown when fatal binding errors occur.
    // MethodArgumentNotValidException: This exception is thrown when argument
    // annotated with @Valid failed validation:
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ResponseError responseError = new ResponseError()
                .rpCode("MethodArgumentNotValidException")
                .rpStatus(status.toString())
                .rpMessage(messageSource
                        .getMessage("MethodArgumentNotValidException.message",
                                null,
                                "",
                                new Locale(httpUtils.getLanguage(request))))
                .detailMessage(exception.getLocalizedMessage());
        return ResponseEntity
                .status(status)
                .body(responseError);
    }

    // bean validation error
    @SuppressWarnings("rawtypes")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception, WebRequest webRequest) {
        ResponseError responseError = new ResponseError()
                .rpCode("ConstraintViolationException")
                .rpStatus("400")
                .rpMessage(messageSource
                        .getMessage("ConstraintViolationException.message",
                                null,
                                "",
                                new Locale(httpUtils.getLanguage(webRequest))))
                .detailMessage(exception.getLocalizedMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseError);
    }

    // MissingServletRequestPartException: This exception is thrown when when the part of a multipart request not found
    // MissingServletRequestParameterException: This exception is thrown when request missing parameter:
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        ResponseError responseError = new ResponseError()
                .rpCode("MissingServletRequestParameterException")
                .rpStatus(status.toString())
                .rpMessage(exception.getParameterName() +
                        " " +
                        messageSource
                                .getMessage("MissingServletRequestParameterException.message",
                                        null,
                                        "",
                                        new Locale(httpUtils.getLanguage(request))))
                .detailMessage(exception.getLocalizedMessage());
        return ResponseEntity
                .status(status)
                .body(responseError);
    }

    //
    // TypeMismatchException: This exception is thrown when try to set bean property with wrong type.
    // MethodArgumentTypeMismatchException: This exception is thrown when method argument is not the expected type:
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception, WebRequest webRequest) {
        ResponseError responseError = new ResponseError()
                .rpCode("MethodArgumentTypeMismatchException")
                .rpStatus("400")
                .rpMessage(exception.getName() +
                        " " +
                        messageSource
                                .getMessage("MethodArgumentTypeMismatchException.message",
                                        null,
                                        "",
                                        new Locale(httpUtils.getLanguage(webRequest))) +
                        exception.getRequiredType().getName())
                .detailMessage(exception.getLocalizedMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseError);
    }

    // xử lý tất cả exception
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseError> handleAll(
            Exception exception,
            WebRequest webRequest) {

        ResponseError responseError = new ResponseError()
                .rpCode("defaultMessage")
                .rpStatus("500")
                .rpMessage(messageSource
                        .getMessage("defaultMessage",
                                null,
                                "",
                                new Locale(httpUtils.getLanguage(webRequest))))
                .detailMessage(exception.getLocalizedMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseError);
    }
}
