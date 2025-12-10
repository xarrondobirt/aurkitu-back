package eus.birt.dam.aurkitu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import eus.birt.dam.aurkitu.dto.ObjetoDTO;
import eus.birt.dam.aurkitu.model.ColorEntity;
import eus.birt.dam.aurkitu.model.ObjetoEntity;
import eus.birt.dam.aurkitu.model.TipoObjetoEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;

/**
 * Mapper para objetos
 */
@Mapper
public abstract class ObjetoMapper {

	public static final ObjetoMapper MAPPER = Mappers.getMapper(ObjetoMapper.class);
	public static final UbicacionMapper UBICACION_MAPPER = Mappers.getMapper(UbicacionMapper.class);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "usuario", source = "usuario")
	@Mapping(target = "color", source = "color")
	@Mapping(target = "ubicacion", expression = "java(UBICACION_MAPPER.toPoint(source.getUbicacion()))")
	@Mapping(target = "radio", source = "source.radio", defaultValue = "0")
	@Mapping(target = "lastUpdateDate", expression = "java(java.time.Instant.now())")
	public abstract ObjetoEntity toEntity(ObjetoDTO source, ColorEntity color, TipoObjetoEntity tipoObjeto,
			UsuarioEntity usuario);

}