package eus.birt.dam.aurkitu.objeto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import eus.birt.dam.aurkitu.dto.ClaveValorDTO;
import eus.birt.dam.aurkitu.dto.ObjetoDTO;
import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.objeto.service.ObjetoService;
import eus.birt.dam.aurkitu.payload.request.BuscarObjetoRequest;
import eus.birt.dam.aurkitu.payload.response.BuscarObjetoResponse;
import eus.birt.dam.aurkitu.payload.response.ConversacionDetalleResponse;
import eus.birt.dam.aurkitu.payload.response.MensajeInfoResponse;
import eus.birt.dam.aurkitu.security.jwt.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
@Tag(name = "02 - Objetos", description = "Gestión de objetos perdidos/encontrados")
public class ObjetoController {

	private final ObjetoService objetoService;
	private final JwtUtils jwtUtils;

	/**
	 * Obtiene la lista de tipos de objeto disponibles
	 * 
	 * @param request Solicitud HTTP para validación de token
	 * @return ResponseEntity con la lista de tipos de objeto
	 */
	@Operation(summary = "Obtener tipos de objeto", description = "Devuelve la lista de tipos de objeto disponibles en el sistema")
	@GetMapping("/obtener-tipos")
	public ResponseEntity<List<ClaveValorDTO>> obtenerTiposObjeto(HttpServletRequest request) {

		// Comprobar usuario
		SesionDTO sesion = jwtUtils.getSesionFromRequest(request);

		log.info("OBJETO - CONTROLLER - OBTENER TIPOS OBJETO - header: {}", sesion.toString());

		return new ResponseEntity<>(objetoService.obtenerTiposObjeto(), HttpStatus.OK);

	}

	/**
	 * Obtiene la lista de colores disponibles para objetos
	 * 
	 * @param request Solicitud HTTP para validación de token
	 * @return ResponseEntity con la lista de colores
	 */
	@Operation(summary = "Obtener colores disponibles", description = "Devuelve la lista de colores disponibles para los objetos")
	@GetMapping("/obtener-colores")
	public ResponseEntity<List<ClaveValorDTO>> obtenerColores(HttpServletRequest request) {

		// Comprobar usuario
		SesionDTO sesion = jwtUtils.getSesionFromRequest(request);

		log.info("OBJETO - CONTROLLER - OBTENER COLORES - header: {}", sesion.toString());

		return new ResponseEntity<>(objetoService.obtenerColores(), HttpStatus.OK);

	}

	/**
	 * Obtiene la lista de estados disponibles para objetos
	 * 
	 * @param request Solicitud HTTP para validación de token
	 * @return ResponseEntity con la lista de estados de objeto
	 */
	@Operation(summary = "Obtener estados de objeto", description = "Devuelve la lista de estados disponibles para objetos")
	@GetMapping("/obtener-estados")
	public ResponseEntity<List<ClaveValorDTO>> obtenerEstados(HttpServletRequest request) {

		// Comprobar usuario
		SesionDTO sesion = jwtUtils.getSesionFromRequest(request);

		log.info("OBJETO - CONTROLLER - OBTENER ESTADOS - header: {}", sesion.toString());

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
	@Operation(summary = "Guardar objeto", description = "Registra un objeto perdido")
	@PostMapping(value = "/guardar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<MensajeInfoResponse> guardarObjeto(HttpServletRequest request,
			@Valid @RequestPart("objeto") ObjetoDTO objeto,
			@RequestPart(value = "foto", required = false) MultipartFile foto,
			@RequestPart(value = "factura", required = false) MultipartFile factura) {

		// Comprobar usuario
		SesionDTO sesion = jwtUtils.getSesionFromRequest(request);

		log.info("OBJETO - CONTROLLER - GUARDAR - header: {} - objeto: {} - foto: {} - factura: {}", sesion.toString(),
				objeto.toString(), foto != null ? foto.getOriginalFilename() : "sin foto",
				factura != null ? factura.getOriginalFilename() : "sin factura");

		return new ResponseEntity<>(objetoService.guardarObjeto(objeto, sesion, foto, factura), HttpStatus.OK);

	}

	/**
	 * Busca objetos aplicando múltiples filtros
	 * 
	 * @param request Solicitud HTTP para validación de token
	 * @param filtros Objeto con todos los criterios de búsqueda aplicables
	 * @return ResponseEntity con la lista de objetos que coinciden con los filtros
	 */
	@PostMapping("/buscar")
	@Operation(summary = "Buscar objetos", description = "Busca objetos perdidos con filtros")
	public ResponseEntity<List<BuscarObjetoResponse>> buscarObjetos(HttpServletRequest request,
			@Valid @RequestBody BuscarObjetoRequest filtros) {

		SesionDTO sesion = jwtUtils.getSesionFromRequest(request);

		log.info("OBJETO - CONTROLLER - BUSCAR - header: {} - filtros: {}", sesion.toString(), filtros.toString());

		return ResponseEntity.ok(objetoService.buscarObjetos(filtros, sesion));
	}

	/**
	 * Muestra la pantalla de chat entre usuarios para un objeto
	 * 
	 * @param idUsuario identificador del usuario con el que se establece el chat
	 * @param idObjeto  identificador del objeto sobre el que se establece el chat
	 * @return ResponseEntity con la lista de mensajes del chat
	 */
	@Operation(summary = "Ver chat", description = "Muestra la pantalla de chat entre usuarios para un objeto")
	@GetMapping("/ver-chat")
	public ResponseEntity<ConversacionDetalleResponse> verChat(HttpServletRequest request,
			@RequestParam @NotNull Integer idUsuario, @RequestParam @NotNull Integer idObjeto) {

		// Comprobar usuario
		SesionDTO sesion = jwtUtils.getSesionFromRequest(request);

		log.info("OBJETO - CONTROLLER - VER CHAT - header: {} - idUsuario: {} - idObjeto: {}", sesion.toString(),
				idUsuario, idObjeto);

		return new ResponseEntity<>(objetoService.verChat(sesion, idUsuario, idObjeto), HttpStatus.OK);

	}
}