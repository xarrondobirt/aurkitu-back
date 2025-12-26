package eus.birt.dam.aurkitu.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import eus.birt.dam.aurkitu.model.ObjetoEntity;
import eus.birt.dam.aurkitu.payload.response.BuscarObjetoResponse;

/**
 * Mapper para búsqueda de objetos
 */
@Mapper
public abstract class BuscarObjetoMapper {

	public static final BuscarObjetoMapper MAPPER = Mappers.getMapper(BuscarObjetoMapper.class);
	public static final UbicacionMapper UBICACION_MAPPER = Mappers.getMapper(UbicacionMapper.class);
	public static final ClaveValorMapper CLAVE_VALOR_MAPPER = Mappers.getMapper(ClaveValorMapper.class);
	public static final SesionMapper SESION_MAPPER = Mappers.getMapper(SesionMapper.class);

	@Mapping(target = "ubicacion", expression = "java(UBICACION_MAPPER.toDTO(source.getUbicacion()))")
	@Mapping(target = "tipo", expression = "java(CLAVE_VALOR_MAPPER.tipoObjetoToDTO(source.getTipo()))")
	@Mapping(target = "color", expression = "java(CLAVE_VALOR_MAPPER.colorTotoDTO(source.getColor()))")
	@Mapping(target = "estado", expression = "java(CLAVE_VALOR_MAPPER.estadoTotoDTO(source.getEstado()))")
	@Mapping(target = "usuario", expression = "java(SESION_MAPPER.toDTO(source.getUsuario()))")
	public abstract BuscarObjetoResponse toResponse(ObjetoEntity source);

	public abstract List<BuscarObjetoResponse> toResponseList(List<ObjetoEntity> source);

}