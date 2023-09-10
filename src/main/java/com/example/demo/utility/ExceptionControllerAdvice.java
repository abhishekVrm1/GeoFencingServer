package com.example.demo.utility;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exception.GeofencingException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

	@ExceptionHandler(GeofencingException.class)
	public ResponseEntity<ErrorInfo> geofencingExceptionHandler(GeofencingException ex) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.name());
		errorInfo.setTimeStamp(LocalDateTime.now());
		errorInfo.setErrorMessage(ex.getMessage());
		return new ResponseEntity<ErrorInfo>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo> generalExceptionHandler(Exception ex) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.name());
		errorInfo.setTimeStamp(LocalDateTime.now());
		errorInfo.setErrorMessage(ex.getMessage());
		return new ResponseEntity<ErrorInfo>(errorInfo, HttpStatus.BAD_REQUEST);
	}
}
