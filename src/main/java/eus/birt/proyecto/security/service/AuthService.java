package eus.birt.proyecto.security.service;

import eus.birt.proyecto.dto.UsuarioDTO;
import eus.birt.proyecto.payload.request.LoginRequest;
import eus.birt.proyecto.payload.request.RegistroUsuarioRequest;
import eus.birt.proyecto.payload.response.LoginResponse;
import eus.birt.proyecto.payload.response.MensajeResponse;
import eus.birt.proyecto.payload.response.RegistroUsuarioResponse;

/**
 * Interfaz que define las operaciones relacionadas con la gestión de usuarios
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

	void logout(String authHeader);
}
