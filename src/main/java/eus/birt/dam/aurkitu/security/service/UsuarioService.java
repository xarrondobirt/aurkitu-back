package eus.birt.dam.aurkitu.security.service;

<<<<<<<< HEAD:src/main/java/eus/birt/dam/aurkitu/security/service/AuthService.java
import eus.birt.proyecto.dto.UsuarioDTO;
import eus.birt.proyecto.payload.request.LoginRequest;
import eus.birt.proyecto.payload.request.RegistroUsuarioRequest;
import eus.birt.proyecto.payload.response.LoginResponse;
import eus.birt.proyecto.payload.response.MensajeResponse;
import eus.birt.proyecto.payload.response.RegistroUsuarioResponse;
========
import eus.birt.dam.aurkitu.dto.UsuarioDTO;
import eus.birt.dam.aurkitu.payload.request.RegistroUsuarioRequest;
import eus.birt.dam.aurkitu.payload.response.MensajeResponse;
import eus.birt.dam.aurkitu.payload.response.RegistroUsuarioResponse;
>>>>>>>> develop:src/main/java/eus/birt/dam/aurkitu/security/service/UsuarioService.java

/**
 * Interfaz que define las operaciones relacionadas con la gestión de usuarios
 */
<<<<<<<< HEAD:src/main/java/eus/birt/dam/aurkitu/security/service/AuthService.java
public interface AuthService {
========
public interface UsuarioService {
>>>>>>>> develop:src/main/java/eus/birt/dam/aurkitu/security/service/UsuarioService.java

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

	LoginResponse refreshToken(String authHeader);
}
