package eus.birt.proyecto.security.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.birt.proyecto.dto.UsuarioDTO;
import eus.birt.proyecto.exception.UnauthorizedException;
import eus.birt.proyecto.payload.response.MensajeResponse;
import eus.birt.proyecto.security.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller para la autenticación
 */
@RestController
@RequestMapping("/v1/auth")
@Validated
@Slf4j
public class AuthController {

	@Autowired
	private UserServiceImpl userService;

	/**
	 * Inicia sesión y devuelve un token JWT.
	 *
	 * @param request      HttpServletRequest
	 * @param loginRequest La solicitud de inicio de sesión.
	 * @param br           Validación de datos.
	 * @return Datos del usuario
	 * @throws UnauthorizedException Si la autenticación falla.
	 * @throws BadRequestException   Si la solicitud es incorrecta.
	 */
	@PostMapping("/registro")
	public ResponseEntity<MensajeResponse> registrarUsuario(HttpServletRequest request,
			@Valid @RequestBody UsuarioDTO usuarioDTO) {
		log.info("AUTH - CONTROLLER - REGISTRO");

//		UsuarioDTO usuario = new UsuarioDTO(loginRequest);

//		UsuarioDTO authorities = userService.accesoSic(usuario, br);

		return new ResponseEntity<>(userService.registrarUsuario(usuarioDTO), HttpStatus.OK);

//		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, authorities.getJwtCookie())
//				.header(HttpHeaders.SET_COOKIE, authorities.getJwtRefreshCookie())
//				.body(new UserInfoResponse(authorities.getNombre(), authorities.getApellidos(), authorities.getEmail(),
//						authorities.getUsername(), authorities.getResponsabilidades()));

	}

	/**
	 * Activa una responsabilidad para el usuario conectado.
	 *
	 * @param requestResp           La HttpServletRequest HTTP.
	 * @param responsabilityRequest La solicitud para activar una responsabilidad.
	 * @return Token JWT actualizado.
	 * @throws ExpectationFaildedException Si la operación falla.
	 */
//	@PostMapping("/activarRespon")
//	public ResponseEntity<Void> selectResponsibility(HttpServletRequest requestResp,
//			@Valid @RequestBody ResponsabilityRequest responsabilityRequest) {
//		log.info("SECURITY - CONTROLLER - ACTIVAR RESPONSABILIDAD");
//
//		Long loginSic = jwtUtils.getLoginSicFromToken(jwtUtils.getJwtFromCookies(requestResp));
//
//		SesionDTO sesion = new SesionDTO(loginSic, responsabilityRequest.getIdResponsabilidad(),
//				responsabilityRequest.getClaveResponsabilidad());
//		userService.respSic(sesion);
//
//		// Si ha ido bien, se añade el idResponsabilidad y la clave a la cookie
//		return ResponseEntity.ok()
//				.header(HttpHeaders.SET_COOKIE,
//						jwtUtils.actualizarResponsabilidadJwtCookie(jwtUtils.getJwtFromCookies(requestResp),
//								responsabilityRequest.getIdResponsabilidad(),
//								responsabilityRequest.getClaveResponsabilidad()).toString())
//				.build();
//
//	}

	/**
	 * Refresca el token JWT del usuario.
	 *
	 * @param requestRefToken HttpServletRequest
	 * @return Token JWT actualizado.
	 * @throws UnauthorizedException Si la actualización del token falla.
	 */
//	@PostMapping("/refreshToken")
//	public ResponseEntity<Void> refreshToken(HttpServletRequest requestRefToken) {
//		log.info("SECURITY - CONTROLLER - REFRESH TOKEN");
//
//		String refreshToken = jwtUtils.getJwtRefreshFromCookies(requestRefToken);
//		SesionDTO sesion = jwtUtils.getSesionFromToken(jwtUtils.getJwtFromCookies(requestRefToken));
//		UsuarioDTO data = refreshTokenService.findByToken(refreshToken, sesion);
//
//		return ResponseEntity.ok()
//				.header(HttpHeaders.SET_COOKIE, data.getJwtCookie())
//				.header(HttpHeaders.SET_COOKIE, data.getJwtRefreshCookie())
//				.build();
//	}

	/**
	 * Cierra la sesión del usuario.
	 *
	 * @param jwtRequest HttpServletRequest
	 * @return Token JWT borrados.
	 */
//	@PostMapping("/logout")
//	public ResponseEntity<Void> closeSesion(HttpServletRequest jwtRequest) {
//		log.info("SECURITY - CONTROLLER - LOGOUT");
//
//		sessionService.logout(jwtRequest);
//
//		String token = jwtUtils.getJwtFromCookies(jwtRequest);
//		Long loginSic = jwtUtils.getLoginSicFromToken(token);
//		if (loginSic != null) {
//			SesionEntity refreshToken = sessionService.getByLoginsicsesion(loginSic);
//			refreshTokenService.deleteByToken(refreshToken.getJwtres());
//		}
//
//		ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
//		ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();
//
//		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//				.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString()).build();
//
//	}
}