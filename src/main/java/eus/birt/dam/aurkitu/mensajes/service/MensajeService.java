package eus.birt.dam.aurkitu.mensajes.service;

import java.util.List;

import eus.birt.dam.aurkitu.dto.MensajeDTO;
import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.payload.request.EnviarMensajeRequest;
import eus.birt.dam.aurkitu.payload.response.ConversacionResponse;
import eus.birt.dam.aurkitu.payload.response.MensajeResponse;

/**
 * Interfaz que define las operaciones relacionadas con los mensajes entre usuarios
 * 
 */
public interface MensajeService {

	MensajeResponse enviarMensaje(SesionDTO sesion, EnviarMensajeRequest msgRequest);

	List<ConversacionResponse> obtenerConversacionesUsuario(SesionDTO sesion);

	List<MensajeDTO> obtenerMensajes(Integer idConversacion, SesionDTO sesion);

}
