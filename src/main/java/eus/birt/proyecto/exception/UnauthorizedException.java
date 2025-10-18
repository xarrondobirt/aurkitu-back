package eus.birt.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para representar una respuesta HTTP UNAUTHORIZED
 * (código 401). Hereda de {@link CustomResponseStatusException}
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends CustomResponseStatusException {

	private static final long serialVersionUID = 1L;

	public UnauthorizedException(String message) {
		super(HttpStatus.UNAUTHORIZED, message);
	}
}
