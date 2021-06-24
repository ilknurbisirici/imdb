package com.vitelco.imdb.exceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public final ResponseEntity<BadRequestException> handleBadRequestExceptions(BadRequestException ex) {
		return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidArgumentException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public final ResponseEntity<InvalidArgumentException> handleNoContentExceptions(InvalidArgumentException ex) {
		return new ResponseEntity<>(ex, HttpStatus.NOT_ACCEPTABLE);
	}


}
