package eus.birt.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para representar una respuesta HTTP NOT FOUND (código
 * 404). Hereda de {@link CustomResponseStatusException}
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends CustomResponseStatusException {

	private static final long serialVersionUID = 1L;

	public NotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}
}
