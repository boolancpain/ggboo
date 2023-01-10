package com.fyo.ggboo.infra.exception;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fyo.ggboo.global.response.BaseResponse;
import com.fyo.ggboo.global.response.ValidationResponse;
import com.fyo.ggboo.infra.message.CustomMessageSource;

/**
 * Custom Exception Handler class
 * 
 * @author boolancpain
 */
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private CustomMessageSource customMessageSource;
	
	/**
	 * ModelAttribute, RequestBody validation 오류 처리
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<ValidationResponse> validationResponses = ex.getFieldErrors().stream()
				.map(fieldError -> ValidationResponse.builder()
						.field(fieldError.getField())
						.message(fieldError.getDefaultMessage())
						.build())
				.collect(Collectors.toList());
		
		// 400
		return ResponseEntity.badRequest().body(validationResponses);
	}
	
	/**
	 * RequestParam validation 오류 처리
	 */
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<ValidationResponse> validationResponses = ex.getFieldErrors().stream()
				.map(fieldError -> ValidationResponse.builder()
						.field(fieldError.getField())
						.message(fieldError.getDefaultMessage())
						.build())
				.collect(Collectors.toList());
		
		// 400
		return ResponseEntity.badRequest().body(validationResponses);
	}
	
	/**
	 * Badrequest handler
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.builder().message(customMessageSource.getMessage("badrequest")).build());
	}
	
	/**
	 * EntityNotFoundException handler
	 * 
	 * @param ex
	 */
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<BaseResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
		// 404
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.builder().message(ex.getMessage()).build());
	}
}