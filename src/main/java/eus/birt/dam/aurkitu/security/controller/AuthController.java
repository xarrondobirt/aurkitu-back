package eus.birt.dam.aurkitu.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.birt.dam.aurkitu.dto.UsuarioDTO;
import eus.birt.dam.aurkitu.payload.request.LoginRequest;
import eus.birt.dam.aurkitu.payload.request.RecuperarPasswordRequest;
import eus.birt.dam.aurkitu.payload.request.RefreshTokenRequest;
import eus.birt.dam.aurkitu.payload.request.RegistroUsuarioRequest;
import eus.birt.dam.aurkitu.payload.request.ResetPasswordRequest;
import eus.birt.dam.aurkitu.payload.response.LoginResponse;
import eus.birt.dam.aurkitu.payload.response.MensajeResponse;
import eus.birt.dam.aurkitu.payload.response.RegistroUsuarioResponse;
import eus.birt.dam.aurkitu.security.service.AuthService;
import eus.birt.dam.aurkitu.utils.Constantes;
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

	/**
	 * Cierra la sesión del usuario invalidando su token JWT
	 * 
	 * @param request Solicitud HTTP que contiene el header de autorización
	 * @return ResponseEntity con estado OK (200)
	 */
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request) {

		log.info("AUTH - CONTROLLER - LOGOUT");

		String authHeader = request.getHeader(Constantes.AUTH);
		authService.logout(authHeader);
		return ResponseEntity.ok().build();
	}

	/**
	 * Refresca el token JWT del usuario.
	 *
	 * @param requestRefToken HttpServletRequest
	 * @return Token JWT actualizado.
	 * @throws UnauthorizedException Si la actualización del token falla.
	 */
	@PostMapping("/refresh-token")
	public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenReq) {

		log.info("AUTH - CONTROLLER - REFRESH TOKEN");

		return ResponseEntity.ok(authService.refreshToken(refreshTokenReq));
	}

	/**
	 * Genera un código y lo envía por mail al usuario para recuperar la contraseña
	 * 
	 * @param request Datos del usuario que quiere recuperar la contraseña
	 * @return Mensaje informativo
	 */
	@PostMapping("/recuperar-password")
	public ResponseEntity<MensajeResponse> forgotPassword(@RequestBody RecuperarPasswordRequest request) {

		log.info("AUTH - CONTROLLER - RECUPERAR PASSWORD");

		return ResponseEntity.ok(authService.recuperarPassword(request.getEmail()));
	}

	/**
	 * Restablece la contraseña del usuario
	 * 
	 * @param request Datos para restablecer la contraseña
	 * @return Mensaje informativo
	 */
	@PostMapping("/reset-password")
	public ResponseEntity<MensajeResponse> resetPassword(@RequestBody ResetPasswordRequest request) {

		log.info("AUTH - CONTROLLER - RESET PASSWORD");

		return ResponseEntity.ok(authService.resetPassword(request));
	}
}