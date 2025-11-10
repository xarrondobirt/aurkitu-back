package eus.birt.dam.aurkitu.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import eus.birt.dam.aurkitu.dto.ClaveValorDTO;
import eus.birt.dam.aurkitu.model.ColorEntity;
import eus.birt.dam.aurkitu.model.TipoObjetoEntity;

/**
 * Mapper para mapeos clave-valor
 */
@Mapper
public abstract class ClaveValorMapper {

	public static final ClaveValorMapper MAPPER = Mappers.getMapper(ClaveValorMapper.class);

	@Mapping(target = "descripcion", source = "codigo")
	public abstract ClaveValorDTO tipoObjetoToDTO(TipoObjetoEntity source);

	public abstract List<ClaveValorDTO> tipoObjetoToDTOList(List<TipoObjetoEntity> source);

	@Mapping(target = "descripcion", source = "codigo")
	public abstract ClaveValorDTO colorTotoDTO(ColorEntity source);

	public abstract List<ClaveValorDTO> colorToDTOList(List<ColorEntity> source);
}