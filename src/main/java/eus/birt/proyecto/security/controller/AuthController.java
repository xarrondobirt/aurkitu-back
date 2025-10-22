package eus.birt.proyecto.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.birt.proyecto.dto.UsuarioDTO;
import eus.birt.proyecto.payload.request.LoginRequest;
import eus.birt.proyecto.payload.request.RegistroUsuarioRequest;
import eus.birt.proyecto.payload.response.LoginResponse;
import eus.birt.proyecto.payload.response.MensajeResponse;
import eus.birt.proyecto.payload.response.RegistroUsuarioResponse;
import eus.birt.proyecto.security.service.AuthService;
import eus.birt.proyecto.utils.Constantes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller para la autenticación
 */
@RestController
@RequestMapping("/v1/auth")
@Validated
@Slf4j
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	/**
	 * Endpoint POST para el registro de usuario
	 * 
	 * @param usuarioDTO Request con los datos del usuario
	 * @return Mensaje informativo
	 */
	@PostMapping("/registro")
	public ResponseEntity<RegistroUsuarioResponse> registrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
		log.info("AUTH - CONTROLLER - REGISTRO");

		return new ResponseEntity<>(authService.registrarUsuario(usuarioDTO), HttpStatus.OK);

	}

	/**
	 * Endpoint POST para verificar el email y completar el registro
	 * 
	 * @param request Datos con el idUsuario y el código de verificación
	 * @return Mensaje informativo
	 */
	@PostMapping("/verificar-email")
	public ResponseEntity<MensajeResponse> verificarEmail(@Valid @RequestBody RegistroUsuarioRequest request) {
		log.info("AUTH - CONTROLLER - VERIFICAR EMAIL");

		return ResponseEntity.ok(authService.verificarCodigo(request));
	}

	/**
	 * Endpoint POST para iniciar la sesión
	 * 
	 * @param loginRequest Datos del usuario para iniciar sesión
	 * @return AccessToken generado
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		log.info("AUTH - CONTROLLER - LOGIN");

		LoginResponse response = authService.login(loginRequest);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request) {
		log.info("AUTH - CONTROLLER - LOGOUT");

		String authHeader = request.getHeader(Constantes.AUTH);
		authService.logout(authHeader);
		return ResponseEntity.ok().build();
	}
}