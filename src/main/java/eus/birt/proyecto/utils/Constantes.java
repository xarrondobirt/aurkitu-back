package eus.birt.proyecto.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constantes utilizadas en la aplicación
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constantes {

	public static final String ERROR_PATH = "Error en el path {}: {}";
	public static final String USUARIO_SIN_VERIFICAR = "Usuario registrado correctamente. Revisa tu email para verificar la cuenta.";
	public static final String APP_NAME = "PROY01";
	public static final String EMAIL_VERIFICADO = "Email verificado correctamente. Ya puedes iniciar sesión en la aplicación.";
	public static final String AUTH = "Authorization";
	public static final String BEARER = "Bearer ";
	public static final String LOGOUT = "Bearer ";
}
