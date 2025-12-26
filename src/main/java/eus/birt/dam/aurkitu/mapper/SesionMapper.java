package eus.birt.dam.aurkitu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.model.UsuarioEntity;

/**
 * Mapper para sesión de usuario
 */
@Mapper
public abstract class SesionMapper {

	public static final SesionMapper MAPPER = Mappers.getMapper(SesionMapper.class);

	public abstract SesionDTO toDTO(UsuarioEntity source);

}