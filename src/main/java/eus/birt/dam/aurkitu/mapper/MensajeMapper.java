package eus.birt.dam.aurkitu.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import eus.birt.dam.aurkitu.dto.MensajeDTO;
import eus.birt.dam.aurkitu.model.MensajeEntity;

/**
 * Mapper para mensajes
 */
@Mapper
public abstract class MensajeMapper {

	public static final MensajeMapper MAPPER = Mappers.getMapper(MensajeMapper.class);
	public static final SesionMapper SESION_MAPPER = Mappers.getMapper(SesionMapper.class);

	@Mapping(target = "remitente", expression = "java(SESION_MAPPER.toDTO(source.getRemitente()))")
	public abstract MensajeDTO toDTO(MensajeEntity source);

	public abstract List<MensajeDTO> toListDTO(List<MensajeEntity> source);

}