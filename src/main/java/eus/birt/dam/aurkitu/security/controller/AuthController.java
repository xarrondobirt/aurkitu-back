package eus.birt.dam.aurkitu.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.birt.dam.aurkitu.dto.UsuarioDTO;
import eus.birt.dam.aurkitu.payload.request.RegistroUsuarioRequest;
import eus.birt.dam.aurkitu.payload.response.MensajeResponse;
import eus.birt.dam.aurkitu.payload.response.RegistroUsuarioResponse;
import eus.birt.dam.aurkitu.security.service.UsuarioService;
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

	private final UsuarioService usuarioService;

	/**
	 * Endpoint POST para el registro de usuario
	 * 
	 * @param usuarioDTO Request con los datos del usuario
	 * @return Mensaje informativo
	 */
	@PostMapping("/registro")
	public ResponseEntity<RegistroUsuarioResponse> registrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
		log.info("AUTH - CONTROLLER - REGISTRO");

		return new ResponseEntity<>(usuarioService.registrarUsuario(usuarioDTO), HttpStatus.OK);

	}

	@PostMapping("/verificar-email")
	public ResponseEntity<MensajeResponse> verificarEmail(@Valid @RequestBody RegistroUsuarioRequest request) {
		log.info("AUTH - CONTROLLER - VERIFICAR EMAIL");

		return ResponseEntity.ok(usuarioService.verificarCodigo(request));
	}
}