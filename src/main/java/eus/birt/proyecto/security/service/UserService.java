package eus.birt.proyecto.security.service;

import eus.birt.proyecto.dto.UsuarioDTO;
import eus.birt.proyecto.payload.response.RegistroUsuarioResponse;

/**
 * Interfaz que define las operaciones relacionadas con la gestión de usuarios
 */
public interface UserService {

	/**
	 * Registra un usuario en la aplicación
	 * 
	 * @param usuarioDTO DTO con los datos del usuario
	 * @return Información del usuario registrado
	 */
	RegistroUsuarioResponse registrarUsuario(UsuarioDTO usuarioDTO);

}
