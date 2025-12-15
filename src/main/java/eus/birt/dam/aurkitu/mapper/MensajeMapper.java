package eus.birt.dam.aurkitu.mapper;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import eus.birt.dam.aurkitu.dto.MensajeDTO;
import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.model.MensajeEntity;

/**
 * Mapper para mensajes
 */
@Mapper
public abstract class MensajeMapper {

	public static final MensajeMapper MAPPER = Mappers.getMapper(MensajeMapper.class);
	public static final SesionMapper SESION_MAPPER = Mappers.getMapper(SesionMapper.class);

	@Mapping(target = "remitente", expression = "java(SESION_MAPPER.toDTO(source.getRemitente()))")
	@Mapping(target = "leido", expression = "java(mapLeido(source, sesion))")
	public abstract MensajeDTO toDTO(MensajeEntity source, @Context SesionDTO sesion);

	public abstract List<MensajeDTO> toListDTO(List<MensajeEntity> source, @Context SesionDTO sesion);

	/**
	 * Determina si un mensaje está marcado como leído para un usuario específico
	 * 
	 * @param mensaje entidad del mensaje a verificar
	 * @param sesion  sesión del usuario para el que se verifica
	 * @return true si el mensaje es del usuario o está marcado como leído, false en caso contrario
	 */
	protected boolean mapLeido(MensajeEntity mensaje, SesionDTO sesion) {

		// Si el mensaje es del usuario, siempre está "leído" para él
		if (mensaje.getRemitente().getId().equals(sesion.getId())) {
			return true;
		}

		// Si no, usa el valor real de BD
		return mensaje.isLeido();
	}
}