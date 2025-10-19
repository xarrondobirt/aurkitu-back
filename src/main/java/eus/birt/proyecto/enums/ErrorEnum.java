package eus.birt.proyecto.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum para los errores de la app
 */
@Getter
@RequiredArgsConstructor
public enum ErrorEnum {
	EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email ya registrado"),
	USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Alias no disponible");

	private final HttpStatus status;
	private final String message;

}
