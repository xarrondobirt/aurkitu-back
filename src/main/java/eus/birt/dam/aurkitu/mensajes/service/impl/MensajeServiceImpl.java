package eus.birt.dam.aurkitu.mensajes.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import eus.birt.dam.aurkitu.dto.MensajeDTO;
import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import eus.birt.dam.aurkitu.mapper.ConversacionMapper;
import eus.birt.dam.aurkitu.mapper.MensajeMapper;
import eus.birt.dam.aurkitu.mensajes.persistence.ConversacionRepository;
import eus.birt.dam.aurkitu.mensajes.persistence.MensajeRepository;
import eus.birt.dam.aurkitu.mensajes.service.MensajeService;
import eus.birt.dam.aurkitu.model.ConversacionEntity;
import eus.birt.dam.aurkitu.model.MensajeEntity;
import eus.birt.dam.aurkitu.model.ObjetoEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;
import eus.birt.dam.aurkitu.objeto.persistence.ObjetoRepository;
import eus.birt.dam.aurkitu.payload.request.EnviarMensajeRequest;
import eus.birt.dam.aurkitu.payload.response.ConversacionResponse;
import eus.birt.dam.aurkitu.payload.response.MensajeInfoResponse;
import eus.birt.dam.aurkitu.security.persistence.UsuarioRepository;
import eus.birt.dam.aurkitu.utils.Constantes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio que implementa la interfaz {@link MensajeService}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MensajeServiceImpl implements MensajeService {

	private final ConversacionRepository conversacionRepo;
	private final MensajeRepository mensajeRepo;
	private final UsuarioRepository usuarioRepo;
	private final ObjetoRepository objetoRepo;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public MensajeInfoResponse enviarMensaje(SesionDTO sesion, EnviarMensajeRequest msgRequest) {

		// Obtener usuarios participantes
		UsuarioEntity remitente = usuarioRepo.findById(sesion.getId())
				.orElseThrow(() -> new AurkituException(ErrorEnum.USER_NOT_FOUND));

		UsuarioEntity destinatario = usuarioRepo.findById(msgRequest.getIdDestinatario())
				.orElseThrow(() -> new AurkituException(ErrorEnum.USER_NOT_FOUND));

		ObjetoEntity objeto = objetoRepo.findById(msgRequest.getIdObjeto())
				.orElseThrow(() -> new AurkituException(ErrorEnum.OBJETO_NO_ENCONTRADO));

		ConversacionEntity conversacion = this.obtenerCrearConversacion(remitente, destinatario, objeto);

		MensajeEntity mensaje = new MensajeEntity();
		mensaje.setConversacion(conversacion);
		mensaje.setRemitente(remitente);
		mensaje.setContenido(msgRequest.getContenido());
		mensaje.setCreateDate(Instant.now());
		mensaje.setLastUpdateDate(Instant.now());

		// Actualizar timestamp de conversación
		conversacion.setLastUpdateDate(Instant.now());
		conversacionRepo.save(conversacion);
		mensajeRepo.save(mensaje);

		return new MensajeInfoResponse(Constantes.MENSAJE_ENVIADO);
	}

	/**
	 * Obtiene o crea una conversación entre dos usuarios sobre un objeto específico
	 * 
	 * @param usuario1 primer usuario participante
	 * @param usuario2 segundo usuario participante
	 * @param objeto   objeto sobre el que trata la conversación
	 * @return conversación existente o nueva conversación creada
	 */
	private ConversacionEntity obtenerCrearConversacion(UsuarioEntity usuario1, UsuarioEntity usuario2,
			ObjetoEntity objeto) {

		// Buscar conversación específica
		return conversacionRepo.findByParticipante1AndParticipante2AndObjeto(usuario1, usuario2, objeto)
				.or(() -> conversacionRepo.findByParticipante1AndParticipante2AndObjeto(usuario2, usuario1, objeto))
				.orElseGet(() -> {

					// Crear nueva conversación para este objeto
					ConversacionEntity nuevaConversacion = new ConversacionEntity();
					nuevaConversacion.setParticipante1(usuario1);
					nuevaConversacion.setParticipante2(usuario2);
					nuevaConversacion.setObjeto(objeto);
					nuevaConversacion.setCreateDate(Instant.now());
					nuevaConversacion.setLastUpdateDate(Instant.now());
					return conversacionRepo.save(nuevaConversacion);
				});
	}

	@Override
	public List<ConversacionResponse> obtenerConversacionesUsuario(SesionDTO sesion) {

		Set<ConversacionEntity> conversaciones = conversacionRepo.findByParticipante1IdOrParticipante2Id(sesion.getId(),
				sesion.getId());

		List<ConversacionEntity> ordenadas = new ArrayList<>(conversaciones);
		ordenadas.sort((c1, c2) -> c2.getLastUpdateDate().compareTo(c1.getLastUpdateDate()));

		return ConversacionMapper.MAPPER.toListResponse(ordenadas, sesion);

	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public List<MensajeDTO> obtenerMensajes(Integer idConversacion, SesionDTO sesion) {

		// Obtener conversación
		ConversacionEntity conversacion = conversacionRepo.findById(idConversacion)
				.orElseThrow(() -> new AurkituException(ErrorEnum.CONVERSACION_NO_ENCONTRADA));

		// Marcar como leídos solo los mensajes donde el usuario no es el remitente
		List<MensajeEntity> mensajes = conversacion.getMensajes().stream()
				.filter(m -> !m.isLeido() && !m.getRemitente().getId().equals(sesion.getId())).toList();

		mensajes.forEach(m -> m.setLeido(true));
		mensajeRepo.saveAll(mensajes);

		return MensajeMapper.MAPPER.toListDTO(conversacion.getMensajes(), sesion);

	}
}
