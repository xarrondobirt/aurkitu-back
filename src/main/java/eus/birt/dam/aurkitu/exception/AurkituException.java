package eus.birt.dam.aurkitu.exception;

import java.io.Serial;

import org.springframework.web.server.ResponseStatusException;

import eus.birt.dam.aurkitu.enums.ErrorEnum;

/**
 * Clase base para excepciones personalizadas que representan respuestas HTTP
 * con un código de estado específico. Hereda de {@link ResponseStatusException}
 */
public class AurkituException extends ResponseStatusException {

	@Serial
	private static final long serialVersionUID = 1L;

	public AurkituException(ErrorEnum errorType) {
		super(errorType.getStatus(), errorType.getMessage());
	}
}
