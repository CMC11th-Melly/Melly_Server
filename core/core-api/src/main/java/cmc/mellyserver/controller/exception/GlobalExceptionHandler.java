package cmc.mellyserver.controller.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;

import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import cmc.mellyserver.support.response.ErrorResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidException(MethodArgumentNotValidException e) {

	ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
	BindingResult bindingResult = e.getBindingResult();

	StringBuilder stringBuilder = new StringBuilder();
	for (FieldError fieldError : bindingResult.getFieldErrors()) {
	  stringBuilder.append(fieldError.getField()).append(":");
	  stringBuilder.append(fieldError.getDefaultMessage());
	  stringBuilder.append(", ");
	}

	final ErrorResponse response = ErrorResponse.of(errorCode, e.getBindingResult());
	return new ResponseEntity<>(response, HTTP_STATUS_OK);
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> handleBindException(BindException e) {

	ErrorCode errorCode = ErrorCode.BINDING_ERROR;
	BindingResult bindingResult = e.getBindingResult();

	final ErrorResponse response = ErrorResponse.of(errorCode, bindingResult);
	return new ResponseEntity<>(response, HTTP_STATUS_OK);
  }

  // 비즈니스 예외 일괄 처리
  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
	ErrorCode errorCode = e.getErrorCode();
	ErrorResponse response = ErrorResponse.of(errorCode);
	return new ResponseEntity<>(response, HTTP_STATUS_OK);
  }

  @ExceptionHandler(IOException.class)
  protected ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
	log.error("handleIOException", ex);
	final ErrorResponse response = ErrorResponse.of(ErrorCode.IO_ERROR, ex.getMessage());
	return new ResponseEntity<>(response, HTTP_STATUS_OK);
  }

  @ExceptionHandler(NullPointerException.class)
  protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
	log.error("handleNullPointerException", e);
	final ErrorResponse response = ErrorResponse.of(ErrorCode.NULL_POINT_ERROR, e.getMessage());
	return new ResponseEntity<>(response, HTTP_STATUS_OK);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  protected ResponseEntity<ErrorResponse> handleNoHandlerFoundExceptionException(NoHandlerFoundException e) {
	log.error("handleNoHandlerFoundExceptionException", e);
	final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_FOUND_ERROR, e.getMessage());
	return new ResponseEntity<>(response, HTTP_STATUS_OK);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
	log.error("HttpMessageNotReadableException", ex);
	final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
	return new ResponseEntity<>(response, HTTP_STATUS_OK);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderExceptionException(
	  MissingServletRequestParameterException ex) {
	log.error("handleMissingServletRequestParameterException", ex);
	final ErrorResponse response = ErrorResponse.of(ErrorCode.MISSING_REQUEST_PARAMETER_ERROR, ex.getMessage());
	return new ResponseEntity<>(response, HTTP_STATUS_OK);
  }

  @ExceptionHandler(HttpClientErrorException.BadRequest.class)
  protected ResponseEntity<ErrorResponse> handleBadRequestException(HttpClientErrorException e) {
	log.error("HttpClientErrorException.BadRequest", e);
	final ErrorResponse response = ErrorResponse.of(ErrorCode.BAD_REQUEST_ERROR, e.getMessage());
	return new ResponseEntity<>(response, HTTP_STATUS_OK);
  }

  @ExceptionHandler(JsonParseException.class)
  protected ResponseEntity<ErrorResponse> handleJsonParseExceptionException(JsonParseException ex) {
	log.error("handleJsonParseExceptionException", ex);
	final ErrorResponse response = ErrorResponse.of(ErrorCode.JSON_PARSE_ERROR, ex.getMessage());
	return new ResponseEntity<>(response, HTTP_STATUS_OK);
  }

  @ExceptionHandler(JsonProcessingException.class)
  protected ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
	log.error("handleJsonProcessingException", ex);
	final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
	return new ResponseEntity<>(response, HTTP_STATUS_OK);
  }

  // ===== Resilence4j =====
  @ExceptionHandler(CallNotPermittedException.class)
  public ResponseEntity<ErrorResponse> handleCallNotPermittedException(CallNotPermittedException ex) {
	log.error("callNotPermittedException", ex);
	final ErrorResponse response = ErrorResponse.of(ErrorCode.SERVER_ERROR, ex.getMessage());
	return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // 잡히지 않은 에러들을 일괄 처리
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
	log.error("intenalServerError", ex);
	ErrorResponse response = ErrorResponse.of(ErrorCode.SERVER_ERROR, ex.getMessage());
	return new ResponseEntity<>(response, HTTP_STATUS_OK);
  }

}
