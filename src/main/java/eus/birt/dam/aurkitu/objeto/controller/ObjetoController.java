package eus.birt.dam.aurkitu.objeto.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.birt.dam.aurkitu.dto.UsuarioDTO;
import eus.birt.dam.aurkitu.payload.response.RegistroUsuarioResponse;
import eus.birt.dam.aurkitu.security.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller para los objetos
 */
@RestController
@RequestMapping("/v1/objeto")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ObjetoController {

	private final UsuarioService usuarioService;

	@PostMapping("/guardar")
	public ResponseEntity<RegistroUsuarioResponse> guardarObjeto(@Valid @RequestBody UsuarioDTO usuarioDTO) {
		log.info("OBJETO - CONTROLLER - GUARDAR");

		return new ResponseEntity<>(usuarioService.registrarUsuario(usuarioDTO), HttpStatus.OK);

	}

}