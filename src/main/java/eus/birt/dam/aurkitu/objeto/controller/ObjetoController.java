package eus.birt.dam.aurkitu.objeto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import eus.birt.dam.aurkitu.dto.ClaveValorDTO;
import eus.birt.dam.aurkitu.dto.ObjetoDTO;
import eus.birt.dam.aurkitu.objeto.service.ObjetoService;
import eus.birt.dam.aurkitu.payload.response.MensajeResponse;
import eus.birt.dam.aurkitu.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
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

	private final ObjetoService objetoService;
	private final JwtUtils jwtUtils;

	/**
	 * Obtiene la lista de tipos de objeto disponibles
	 * 
	 * @param request Solicitud HTTP para validación de token
	 * @return ResponseEntity con la lista de tipos de objeto
	 */
	@GetMapping("/obtener-tipos")
	public ResponseEntity<List<ClaveValorDTO>> obtenerTiposObjeto(HttpServletRequest request) {

		log.info("OBJETO - CONTROLLER - OBTENER TIPOS OBJETO");

		// Comprobar usuario
		jwtUtils.getUserIdFromRequest(request);

		return new ResponseEntity<>(objetoService.obtenerTiposObjeto(), HttpStatus.OK);

	}

	/**
	 * Obtiene la lista de colores disponibles para objetos
	 * 
	 * @param request Solicitud HTTP para validación de token
	 * @return ResponseEntity con la lista de colores
	 */
	@GetMapping("/obtener-colores")
	public ResponseEntity<List<ClaveValorDTO>> obtenerColores(HttpServletRequest request) {

		log.info("OBJETO - CONTROLLER - OBTENER COLORES");

		// Comprobar usuario
		jwtUtils.getUserIdFromRequest(request);

		return new ResponseEntity<>(objetoService.obtenerColores(), HttpStatus.OK);

	}

	/**
	 * Obtiene la lista de estados disponibles para objetos
	 * 
	 * @param request Solicitud HTTP para validación de token
	 * @return ResponseEntity con la lista de estados de objeto
	 */
	@GetMapping("/obtener-estados")
	public ResponseEntity<List<ClaveValorDTO>> obtenerEstados(HttpServletRequest request) {

		log.info("OBJETO - CONTROLLER - OBTENER ESTADOS");

		// Comprobar usuario
		jwtUtils.getUserIdFromRequest(request);

		return new ResponseEntity<>(objetoService.obtenerEstadosObjeto(), HttpStatus.OK);

	}

	/**
	 * Endpoint para guardar un objeto perdido
	 * 
	 * @param objetoDTO DTO con la información del objeto
	 * @param foto      fichero con la foto
	 * @param factura   fichero con la factura
	 * @return Mensaje informativo para el usuario
	 */
	@PostMapping(value = "/guardar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<MensajeResponse> guardarObjeto(HttpServletRequest request,
			@Valid @RequestPart("objeto") ObjetoDTO objeto,
			@RequestPart(value = "foto", required = false) MultipartFile foto,
			@RequestPart(value = "factura", required = false) MultipartFile factura) {

		log.info("OBJETO - CONTROLLER - GUARDAR");

		// Comprobar usuario
		Integer idUsuario = jwtUtils.getUserIdFromRequest(request);

		return new ResponseEntity<>(objetoService.guardarObjeto(objeto, idUsuario, foto, factura), HttpStatus.OK);

	}
}