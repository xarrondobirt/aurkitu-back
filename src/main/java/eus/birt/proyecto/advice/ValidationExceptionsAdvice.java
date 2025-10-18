package eus.birt.proyecto.advice;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
 * {@link ConstraintViolationException} Hereda de
 * {@link ResponseEntityExceptionHandler}
 */
@RestControllerAdvice
@Order(2)
@Slf4j
public class ValidationExceptionsAdvice extends ResponseEntityExceptionHandler {

	/**
	 * Maneja excepciones de tipo {@link ConstraintViolationException} y devuelve
	 * una respuesta JSON personalizada.
	 *
	 * @param ex      La excepción de tipo {@link ConstraintViolationException} que
	 *                se ha producido.
	 * @param request La solicitud web que provocó la excepción.
	 * @return JSON personalizado del error.
	 */
	@ExceptionHandler(value = { ConstraintViolationException.class })
	protected ResponseEntity<Object> handleRuntimeException(ConstraintViolationException ex, WebRequest request) {
		String bodyOfResponse = "";
		log.debug("Aviso de Excepcion en back");

		// Obtener el path de la solicitud que causó el error
		String requestPath = ((ServletWebRequest) request).getRequest().getRequestURI();

		// Log detallado de la excepción con el path de la solicitud y el stack trace
		// completo
		log.error("Error en el path {}: {}", requestPath, ex.getMessage(), ex);

		APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "Error interno",
				((ServletWebRequest) request).getRequest());

		ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();

		try {
			bodyOfResponse = mapper.writeValueAsString(apiError);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		((ServletWebRequest) request).getRequest().getRequestURI();

		return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
