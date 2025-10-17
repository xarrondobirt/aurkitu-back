package eus.birt.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Clase base para excepciones personalizadas que representan respuestas HTTP
 * con un código de estado específico. Hereda de {@link ResponseStatusException}
 */
public class CustomResponseStatusException extends ResponseStatusException {

	private static final long serialVersionUID = 1L;

	public CustomResponseStatusException(HttpStatus status, String reason) {
		super(status, reason);
	}

}
