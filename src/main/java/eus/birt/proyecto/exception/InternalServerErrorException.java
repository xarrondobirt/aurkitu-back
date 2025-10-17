package eus.birt.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para representar una respuesta HTTP INTERNAL SERVER
 * ERROR (código 500). Hereda de {@link CustomResponseStatusException}
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends CustomResponseStatusException {

	private static final long serialVersionUID = 1L;

	public InternalServerErrorException(String message) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}
}
