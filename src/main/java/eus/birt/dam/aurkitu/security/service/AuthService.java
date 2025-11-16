package eus.birt.dam.aurkitu.security.service;

import eus.birt.dam.aurkitu.dto.UsuarioDTO;
import eus.birt.dam.aurkitu.payload.request.LoginRequest;
import eus.birt.dam.aurkitu.payload.request.RefreshTokenRequest;
import eus.birt.dam.aurkitu.payload.request.RegistroUsuarioRequest;
import eus.birt.dam.aurkitu.payload.request.ResetPasswordRequest;
import eus.birt.dam.aurkitu.payload.response.LoginResponse;
import eus.birt.dam.aurkitu.payload.response.MensajeResponse;
import eus.birt.dam.aurkitu.payload.response.RegistroUsuarioResponse;

/**
 * Interfaz que define las operaciones relacionadas con la gestión de usuarios
 * 
 */
public interface AuthService {

	/**
	 * Registra un usuario en la aplicación
	 * 
	 * @param usuarioDTO DTO con los datos del usuario
	 * @return Información del usuario registrado
	 */
	RegistroUsuarioResponse registrarUsuario(UsuarioDTO usuarioDTO);

	/**
	 * Verifica el email de un usuario para completar el registro
	 * 
	 * @param request Objeto con el idUsuario y el código de verificación
	 * @return Mensaje informativo.
	 */
	MensajeResponse verificarCodigo(RegistroUsuarioRequest request);

	/**
	 * Inicio sesión del usuario
	 * 
	 * @param request Datos del usuario para el inicio de sesión
	 * @return AccessToken
	 */
	LoginResponse login(LoginRequest request);

	/**
	 * Cierra la sesión
	 * 
	 * @param authHeader Access token del usuario
	 */
	void logout(String authHeader);

	/**
	 * Actualiza el token
	 * 
	 * @param refreshTokenReq Refresh token del usuario
	 * @return Access token renovado
	 */
	LoginResponse refreshToken(RefreshTokenRequest refreshTokenReq);

	/**
	 * Envía un código al usuario para recuperar la contrseña
	 * 
	 * @param email Email del usuario
	 * @return Mensaje informativo
	 */
	MensajeResponse recuperarPassword(String email);

	/**
	 * Resetea la contraseña del usuario
	 * 
	 * @param request Datos para el reseteo de la contraseña
	 * @return Mensaje informativo
	 */
	MensajeResponse resetPassword(ResetPasswordRequest request);
}
