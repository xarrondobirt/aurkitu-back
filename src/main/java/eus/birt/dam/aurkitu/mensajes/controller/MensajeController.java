package eus.birt.dam.aurkitu.mensajes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.birt.dam.aurkitu.dto.MensajeDTO;
import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.mensajes.service.MensajeService;
import eus.birt.dam.aurkitu.payload.request.EnviarMensajeRequest;
import eus.birt.dam.aurkitu.payload.response.ConversacionResponse;
import eus.birt.dam.aurkitu.payload.response.MensajeResponse;
import eus.birt.dam.aurkitu.security.jwt.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller para los mensajes
 */
@RestController
@RequestMapping("/v1/mensaje")
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "03 - Mensajes", description = "Gestión de mensajes y conversaciones entre usuarios")
public class MensajeController {

	private final MensajeService mensajeService;
	private final JwtUtils jwtUtils;

	/**
	 * Envía un mensaje entre usuarios en una conversación
	 * 
	 * @param msgRequest objeto con los datos del mensaje a enviar
	 * @param request    objeto HttpServletRequest para obtener la sesión del usuario
	 * @return ResponseEntity con la respuesta del mensaje enviado
	 */
	@Operation(summary = "Enviar mensaje", description = "Envía un mensaje entre usuarios")
	@PostMapping("/enviar")
	public ResponseEntity<MensajeResponse> enviarMensaje(@RequestBody EnviarMensajeRequest msgRequest,
			HttpServletRequest request) {

		SesionDTO sesion = jwtUtils.getSesionFromRequest(request);

		log.info("MENSAJE - CONTROLLER - ENVIAR MENSAJE - header: {} - mensaje: {}", sesion.toString(),
				msgRequest.toString());

		return new ResponseEntity<>(mensajeService.enviarMensaje(sesion, msgRequest), HttpStatus.CREATED);
	}

	/**
	 * Obtiene todas las conversaciones del usuario autenticado
	 * 
	 * @param request objeto HttpServletRequest para obtener la sesión del usuario
	 * @return ResponseEntity con la lista de conversaciones del usuario
	 */
	@Operation(summary = "Obtener conversaciones", description = "Obtiene todas las conversaciones del usuario autenticado")
	@GetMapping("/conversaciones")
	public ResponseEntity<List<ConversacionResponse>> obtenerConversaciones(HttpServletRequest request) {

		SesionDTO sesion = jwtUtils.getSesionFromRequest(request);

		log.info("MENSAJE - CONTROLLER - OBTENER CONVERSACIONES - header: {}", sesion.toString());

		return new ResponseEntity<>(mensajeService.obtenerConversacionesUsuario(sesion), HttpStatus.OK);
	}

	/**
	 * Obtiene los mensajes de una conversación específica y los marca como leídos
	 * 
	 * @param idConversacion identificador de la conversación
	 * @param request        objeto HttpServletRequest para obtener la sesión del usuario
	 * @return ResponseEntity con la lista de mensajes de la conversación
	 */
	@Operation(summary = "Obtener mensajes de conversación", description = "Obtiene los mensajes de una conversación específica y los marca como leídos")
	@GetMapping("/conversacion/{idConversacion}/mensajes")
	public ResponseEntity<List<MensajeDTO>> obtenerMensajes(@PathVariable Integer idConversacion,
			HttpServletRequest request) {

		SesionDTO sesion = jwtUtils.getSesionFromRequest(request);

		log.info("MENSAJE - CONTROLLER - OBTENER MENSAJES - header: {} - idConversacion: {}", sesion.toString(),
				idConversacion);

		// Este método obtiene y marca como leídos
		List<MensajeDTO> mensajes = mensajeService.obtenerMensajes(idConversacion, sesion);

		return new ResponseEntity<>(mensajes, HttpStatus.OK);
	}
}