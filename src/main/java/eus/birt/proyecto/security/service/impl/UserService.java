package eus.birt.proyecto.security.service.impl;

import eus.birt.proyecto.dto.UsuarioDTO;
import eus.birt.proyecto.exception.UnauthorizedException;
import eus.birt.proyecto.payload.response.MensajeResponse;

/**
 * Interfaz que define las operaciones relacionadas con la gestión de usuarios
 */
public interface UserService {

	/**
	 * Busca un usuario por su nombre de usuario.
	 *
	 * @param username El nombre de usuario a buscar.
	 * @return Un usuario si se encuentra
	 */
//	Optional<UsuarioEntity> findByUsername(String username);

	/**
	 * Realiza un proceso de autenticación y acceso al sistema.
	 *
	 * @param usuario El objeto UsuarioDTO que contiene los datos de usuario
	 * @param br      El objeto BindingResult que contiene los resultados de la
	 *                validación.
	 * @return Un objeto UsuarioDTO con los datos del usuario autenticado.
	 * @throws UnauthorizedException Si la autenticación no es válida.
	 * @throws BadRequestException   Si la solicitud es incorrecta.
	 */
//	UsuarioDTO accesoSic(UsuarioDTO usuario, BindingResult br) throws UnauthorizedException, BadRequestException;
	MensajeResponse registrarUsuario(UsuarioDTO usuarioDTO);

}
