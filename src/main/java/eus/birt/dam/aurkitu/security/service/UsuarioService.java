package eus.birt.dam.aurkitu.security.service;

import eus.birt.dam.aurkitu.dto.UsuarioDTO;
import eus.birt.dam.aurkitu.payload.request.RegistroUsuarioRequest;
import eus.birt.dam.aurkitu.payload.response.MensajeResponse;
import eus.birt.dam.aurkitu.payload.response.RegistroUsuarioResponse;

/**
 * Interfaz que define las operaciones relacionadas con la gestión de usuarios
 */
public interface UsuarioService {

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

}
