package eus.birt.proyecto.advice;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import eus.birt.proyecto.exception.APIError;
import eus.birt.proyecto.utils.ConstantesError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que proporciona manejo de excepciones globales para excepciones
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/**
	 * Maneja excepciones de tipo {@link ResponseStatusException}
	 * 
	 * @param ex      La excepción de tipo {@link ResponseStatusException} que se ha
	 *                producido.
	 * @param request La solicitud web que provocó la excepción.
	 * @return JSON personalizado del error.
	 */
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<APIError> handleResponseStatus(ResponseStatusException ex, HttpServletRequest request) {
		log.error(ConstantesError.ERROR_PATH, request.getRequestURI(), ex.getMessage(), ex);

		HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
		APIError error = new APIError(status.value(), status.getReasonPhrase(), ex.getReason(), request);
		return new ResponseEntity<>(error, status);
	}

	/**
	 * Maneja excepciones de tipo {@link MethodArgumentNotValidException}
	 * 
	 * @param ex      La excepción de tipo {@link MethodArgumentNotValidException}
	 *                que se ha producido.
	 * @param request La solicitud web que provocó la excepción.
	 * @return JSON personalizado del error.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<APIError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
		log.error(ConstantesError.ERROR_PATH, request.getRequestURI(), ex.getMessage(), ex);

		// Coger solo el mensaje personalizado de las anotaciones de los parámetros
		String errorMessage = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getDefaultMessage())
				.collect(Collectors.joining(", "));

		APIError error = new APIError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
				errorMessage, request);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Maneja excepciones de tipo {@link RuntimeException}
	 * 
	 * @param ex      La excepción de tipo {@link RuntimeException} que se ha
	 *                producido.
	 * @param request La solicitud web que provocó la excepción.
	 * @return JSON personalizado del error.
	 */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<APIError> handleRuntime(RuntimeException ex, HttpServletRequest request) {
		log.error(ConstantesError.ERROR_PATH, request.getRequestURI(), ex.getMessage(), ex);

		APIError error = new APIError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage(), request);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}