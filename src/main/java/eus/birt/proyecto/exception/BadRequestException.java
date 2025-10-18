package eus.birt.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para representar una respuesta HTTP BAD REQUEST
 * (código 400). Hereda de {@link CustomResponseStatusException}
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends CustomResponseStatusException {

	private static final long serialVersionUID = 1L;

	public BadRequestException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}
}
