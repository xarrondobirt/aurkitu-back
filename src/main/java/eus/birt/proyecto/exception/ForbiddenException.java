package eus.birt.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para representar una respuesta HTTP FORBIDDEN (código
 * 403). Hereda de {@link CustomResponseStatusException}
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends CustomResponseStatusException {

	private static final long serialVersionUID = 1L;

	public ForbiddenException(String message) {
		super(HttpStatus.FORBIDDEN, message);
	}
}
