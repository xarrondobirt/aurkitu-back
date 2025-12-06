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
public class MensajeController {

	private final MensajeService mensajeService;
	private final JwtUtils jwtUtils;

	@PostMapping("/enviar")
	public ResponseEntity<MensajeResponse> enviarMensaje(@RequestBody EnviarMensajeRequest msgRequest,
			HttpServletRequest request) {

		log.info("MENSAJE - CONTROLLER - ENVIAR MENSAJE");
//		Integer remitente = jwtUtils.getUserIdFromRequest(request);
		SesionDTO sesion = jwtUtils.getSesionFromRequest(request);
//		Usuario destinatario = authService.buscarUsuarioPorId(request.getDestinatarioId());

		MensajeResponse mensaje = mensajeService.enviarMensaje(sesion, msgRequest);

		return new ResponseEntity<>(mensaje, HttpStatus.CREATED);
	}

	@GetMapping("/conversaciones")
	public ResponseEntity<List<ConversacionResponse>> obtenerConversaciones(HttpServletRequest request) {

		log.info("MENSAJE - CONTROLLER - OBTENER CONVERSACIONES");

//		Integer usuario = jwtUtils.getUserIdFromRequest(request);
		SesionDTO sesion = jwtUtils.getSesionFromRequest(request);
		List<ConversacionResponse> conversaciones = mensajeService.obtenerConversacionesUsuario(sesion);

		return new ResponseEntity<>(conversaciones, HttpStatus.OK);
	}

	@GetMapping("/conversacion/{idConversacion}/mensajes")
	public ResponseEntity<List<MensajeDTO>> obtenerYMarcarMensajes(@PathVariable Integer idConversacion,
			HttpServletRequest request) {

		SesionDTO sesion = jwtUtils.getSesionFromRequest(request);

		// Este método obtiene Y marca como leídos
		List<MensajeDTO> mensajes = mensajeService.obtenerMensajes(idConversacion, sesion);

//		return ResponseEntity.ok(mensajes);
		return new ResponseEntity<>(mensajes, HttpStatus.OK);
	}
}