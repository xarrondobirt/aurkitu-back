package eus.birt.dam.aurkitu.mensajes.service;

import java.util.List;

import eus.birt.dam.aurkitu.dto.MensajeDTO;
import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.payload.request.EnviarMensajeRequest;
import eus.birt.dam.aurkitu.payload.response.ConversacionResponse;
import eus.birt.dam.aurkitu.payload.response.MensajeInfoResponse;

/**
 * Interfaz que define las operaciones relacionadas con los mensajes entre usuarios
 * 
 */
public interface MensajeService {

	/**
	 * Envía un mensaje desde un usuario hacia otro usuario o grupo
	 * 
	 * @param sesion     sesión del usuario que envía el mensaje
	 * @param msgRequest datos del mensaje a enviar
	 * @return respuesta con los datos del mensaje enviado
	 */
	MensajeInfoResponse enviarMensaje(SesionDTO sesion, EnviarMensajeRequest msgRequest);

	/**
	 * Obtiene todas las conversaciones en las que participa un usuario
	 * 
	 * @param sesion sesión del usuario para obtener sus conversaciones
	 * @return lista de conversaciones del usuario
	 */
	List<ConversacionResponse> obtenerConversacionesUsuario(SesionDTO sesion);

	/**
	 * Obtiene los mensajes de una conversación específica y los marca como leídos
	 * 
	 * @param idConversacion identificador de la conversación
	 * @param sesion         sesión del usuario que solicita los mensajes
	 * @return lista de mensajes de la conversación
	 */
	List<MensajeDTO> obtenerMensajes(Integer idConversacion, SesionDTO sesion);

}
