package eus.birt.dam.aurkitu.mensajes.service;

import java.util.List;

import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.model.ConversacionEntity;
import eus.birt.dam.aurkitu.model.ObjetoEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;
import eus.birt.dam.aurkitu.payload.request.EnviarMensajeRequest;
import eus.birt.dam.aurkitu.payload.response.ConversacionDetalleResponse;
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
	 * @return id de la conversacion con la lista de mensajes de la conversación
	 */
	ConversacionDetalleResponse obtenerMensajes(Integer idConversacion, SesionDTO sesion);

	/**
	 * Obtiene o crea una conversación entre dos usuarios sobre un objeto específico
	 * 
	 * @param usuario1 primer usuario participante
	 * @param usuario2 segundo usuario participante
	 * @param objeto   objeto sobre el que trata la conversación
	 * @return conversación existente o nueva conversación creada
	 */
	ConversacionEntity obtenerCrearConversacion(UsuarioEntity usuario1, UsuarioEntity usuario2, ObjetoEntity objeto);

}
