package eus.birt.dam.aurkitu.mapper;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.model.ConversacionEntity;
import eus.birt.dam.aurkitu.payload.response.ConversacionResponse;

/**
 * Mapper para conversaciones
 */
@Mapper
public abstract class ConversacionMapper {

	public static final ConversacionMapper MAPPER = Mappers.getMapper(ConversacionMapper.class);
//	public static final MensajeMapper MENSAJE_MAPPER = Mappers.getMapper(MensajeMapper.class);
	public static final SesionMapper SESION_MAPPER = Mappers.getMapper(SesionMapper.class);

	@Mapping(target = "participante", expression = "java(obtenerOtroParticipante(source, sesion))")
//	@Mapping(target = "mensajes", expression = "java(MENSAJE_MAPPER.toListDTO(source.getMensajes))")
	@Mapping(target = "idObjeto", source = "source.objeto.id")
	@Mapping(target = "mensajesSinLeer", expression = "java(tieneMensajesSinLeer(source))")
	public abstract ConversacionResponse toResponse(ConversacionEntity source, @Context SesionDTO sesion);

	public abstract List<ConversacionResponse> toListResponse(List<ConversacionEntity> source,
			@Context SesionDTO sesion);

	protected SesionDTO obtenerOtroParticipante(ConversacionEntity conversacion, SesionDTO sesion) {

		if (conversacion.getParticipante1().getId().equals(sesion.getId())) {
			return SESION_MAPPER.toDTO(conversacion.getParticipante2());
		} else {
			return SESION_MAPPER.toDTO(conversacion.getParticipante1());
		}
	}

	protected boolean tieneMensajesSinLeer(ConversacionEntity conversacion) {
		return conversacion.getMensajes().stream().anyMatch(m -> !m.isLeido());
	}
}