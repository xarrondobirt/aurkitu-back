package eus.birt.dam.aurkitu.enums;

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
	USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Alias no disponible"),
	EMAIL_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Alias no disponible"),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Usuario no encontrado"),
	USERNAME_ALREADY_VERIFIED(HttpStatus.CONFLICT, "Usuario ya verificado"),
	VERIFICATION_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "Código de verificación no encontrado"),
	VERIFICATION_CODE_EXPIRED(HttpStatus.GONE, "Código de verificación caducado"),
	BAD_CREDENTIALS(HttpStatus.BAD_REQUEST, "Credenciales inválidas"),
	SESION_ERROR(HttpStatus.UNAUTHORIZED, "Sesión no válida"),
	REFRESH_TOKEN_INVALIDO(HttpStatus.UNAUTHORIZED, "Refresh token no válido"),
	REFRESH_TOKEN_CADUCADO(HttpStatus.UNAUTHORIZED, "Refresh token caducado"),
	PASSWORD_NO_COINCIDEN(HttpStatus.BAD_REQUEST, "Las contraseñas son diferentes"),
	JWT_AUTH_REQUERIDA(HttpStatus.UNAUTHORIZED, "JWT autorización requerida"),
	TIPO_OBJETO_NO_ENCONTRADO(HttpStatus.NOT_FOUND, "Tipo de objeto no encontrado"),
	COLOR_NO_ENCONTRADO(HttpStatus.NOT_FOUND, "Color no encontrado"),
	ACCES_TOKEN_CADUCADO(HttpStatus.UNAUTHORIZED, "Sesión caducada"),
	ESTADO_NO_ENCONTRADO(HttpStatus.NOT_FOUND, "Estado no encontrado"),
	SUBIR_ARCHIVO(HttpStatus.INTERNAL_SERVER_ERROR, "Error al subir el archivo"),
	OBJETO_NO_ENCONTRADO(HttpStatus.NOT_FOUND, "Objeto no encontrado"),
	CONVERSACION_NO_ENCONTRADA(HttpStatus.NOT_FOUND, "Conversación no encontrada");

	private final HttpStatus status;
	private final String message;

}
