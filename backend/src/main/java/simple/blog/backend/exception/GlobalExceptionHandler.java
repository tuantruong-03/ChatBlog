package simple.blog.backend.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import simple.blog.backend.dto.response.ApiResponse;

// @RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleUnwantedException(Exception e) {
		ApiResponse resp = ApiResponse.builder()
				.timestamp(LocalDateTime.now())
				.message(e.getMessage())
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.build();

		return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AppException.class)
	public ResponseEntity<ApiResponse> handleAppException(AppException e) {
		HttpStatus httpStatus = e.getHttpStatus();
		ApiResponse resp = ApiResponse.builder()
				.timestamp(LocalDateTime.now())
				.message(e.getMessage())
				.statusCode(httpStatus.value())
				.build();

		return new ResponseEntity<>(resp, httpStatus);
	}

	@ExceptionHandler({ ConstraintViolationException.class, MissingServletRequestParameterException.class,
			MethodArgumentNotValidException.class })
	public ResponseEntity<ApiResponse> handleValidationException(Exception e) {

		String message = e.getMessage();

		if (e instanceof MethodArgumentNotValidException) {
			int start = message.lastIndexOf("[") + 1;
			int end = message.lastIndexOf("]") - 1;
			message = message.substring(start, end);
		}

		System.out.println(message);

		ApiResponse resp = ApiResponse.builder()
				.timestamp(LocalDateTime.now())
				.message(message)
				.statusCode(HttpStatus.BAD_REQUEST.value()).build();

		return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
	}

}
