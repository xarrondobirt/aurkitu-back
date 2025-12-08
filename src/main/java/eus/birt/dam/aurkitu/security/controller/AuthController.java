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
import eus.birt.dam.aurkitu.payload.response.MensajeInfoResponse;
import eus.birt.dam.aurkitu.payload.response.RegistroUsuarioResponse;
import eus.birt.dam.aurkitu.security.service.AuthService;
import eus.birt.dam.aurkitu.utils.Constantes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "01 - Autenticación", description = "Endpoints para registro, login y gestión de tokens")
public class AuthController {

	private final AuthService authService;

	/**
	 * Endpoint POST para el registro de usuario
	 * 
	 * @param usuarioDTO Request con los datos del usuario
	 * @return Mensaje informativo
	 */
	@Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario en el sistema. Se enviará un código de verificación por email.")
	@PostMapping("/registro")
	public ResponseEntity<RegistroUsuarioResponse> registrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {

		log.info("AUTH - CONTROLLER - REGISTRO - email: {} - username: {}", usuarioDTO.getEmail(),
				usuarioDTO.getUsername());

		return new ResponseEntity<>(authService.registrarUsuario(usuarioDTO), HttpStatus.OK);

	}

	/**
	 * Endpoint POST para verificar el email y completar el registro
	 * 
	 * @param request Datos con el idUsuario y el código de verificación
	 * @return Mensaje informativo
	 */
	@Operation(summary = "Verificar email", description = "Verifica el email del usuario usando el código enviado")
	@PostMapping("/verificar-email")
	public ResponseEntity<MensajeInfoResponse> verificarEmail(@Valid @RequestBody RegistroUsuarioRequest request) {

		log.info("AUTH - CONTROLLER - VERIFICAR EMAIL - id: {} - codigo: {}", request.getIdUsuario(),
				request.getCodigoVerificacion());

		return ResponseEntity.ok(authService.verificarCodigo(request));
	}

	/**
	 * Endpoint POST para iniciar la sesión
	 * 
	 * @param loginRequest Datos del usuario para iniciar sesión
	 * @return AccessToken generado
	 */
	@Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve tokens JWT")
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

		log.info("AUTH - CONTROLLER - LOGIN - username: {}", loginRequest.getUsername());

		LoginResponse response = authService.login(loginRequest);

		return ResponseEntity.ok(response);
	}

	/**
	 * Cierra la sesión del usuario invalidando su token JWT
	 * 
	 * @param request Solicitud HTTP que contiene el header de autorización
	 * @return ResponseEntity con estado OK (200)
	 */
	@Operation(summary = "Cerrar sesión", description = "Invalida el token JWT del usuario")
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request) {

		String authHeader = request.getHeader(Constantes.AUTH);

		log.info("AUTH - CONTROLLER - LOGOUT - header: {}", authHeader);

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
	@Operation(summary = "Refrescar token", description = "Obtiene un nuevo access token usando el refresh token")
	@PostMapping("/refresh-token")
	public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenReq) {

		log.info("AUTH - CONTROLLER - REFRESH TOKEN - token: {}", refreshTokenReq.getToken());

		return ResponseEntity.ok(authService.refreshToken(refreshTokenReq));
	}

	/**
	 * Genera un código y lo envía por mail al usuario para recuperar la contraseña
	 * 
	 * @param request Datos del usuario que quiere recuperar la contraseña
	 * @return Mensaje informativo
	 */
	@Operation(summary = "Recuperar contraseña", description = "Envía un código por email para recuperar la contraseña")
	@PostMapping("/recuperar-password")
	public ResponseEntity<MensajeInfoResponse> forgotPassword(@RequestBody RecuperarPasswordRequest request) {

		log.info("AUTH - CONTROLLER - RECUPERAR PASSWORD - email: {}", request.getEmail());

		return ResponseEntity.ok(authService.recuperarPassword(request.getEmail()));
	}

	/**
	 * Restablece la contraseña del usuario
	 * 
	 * @param request Datos para restablecer la contraseña
	 * @return Mensaje informativo
	 */
	@Operation(summary = "Restablecer contraseña", description = "Cambia la contraseña usando el código de verificación")
	@PostMapping("/reset-password")
	public ResponseEntity<MensajeInfoResponse> resetPassword(@RequestBody ResetPasswordRequest request) {

		log.info("AUTH - CONTROLLER - RESET PASSWORD - id: {} - codigo: {}", request.getIdUsuario(),
				request.getCodVerificacion());

		return ResponseEntity.ok(authService.resetPassword(request));
	}
}