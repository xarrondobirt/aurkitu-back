package eus.birt.proyecto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para representar una respuesta HTTP EXPECTATION
 * FAILED (código 417). Hereda de {@link CustomResponseStatusException}
 */
@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class ExpectationFaildedException extends CustomResponseStatusException {

	private static final long serialVersionUID = 1L;

	public ExpectationFaildedException(String message) {
		super(HttpStatus.EXPECTATION_FAILED, message);
	}
}
