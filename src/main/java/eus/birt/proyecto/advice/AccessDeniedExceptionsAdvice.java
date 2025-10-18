package eus.birt.proyecto.advice;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import eus.birt.proyecto.exception.APIError;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que proporciona manejo de excepciones globales para excepciones de tipo
 * {@link AccessDeniedException} Hereda de
 * {@link ResponseEntityExceptionHandler}
 */
@RestControllerAdvice
@Order(2)
@Slf4j
public class AccessDeniedExceptionsAdvice extends ResponseEntityExceptionHandler {

	/**
	 * Maneja excepciones de tipo {@link AccessDeniedException} y devuelve una
	 * respuesta JSON personalizada.
	 *
	 * @param ex      La excepción de tipo {@link AccessDeniedException} que se ha
	 *                producido.
	 * @param request La solicitud web que provocó la excepción.
	 * @return JSON personalizado del error.
	 */
	@ExceptionHandler(value = { AccessDeniedException.class })
	protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		String bodyOfResponse = "";
		log.debug("Acceso denegado");

		APIError apiError = new APIError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
				ex.getMessage(), ((ServletWebRequest) request).getRequest());

		ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();

		try {
			bodyOfResponse = mapper.writeValueAsString(apiError);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.FORBIDDEN, request);
	}
}
