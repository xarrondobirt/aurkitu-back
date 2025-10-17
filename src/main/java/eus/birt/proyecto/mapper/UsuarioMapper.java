package eus.birt.proyecto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import eus.birt.proyecto.dto.UsuarioDTO;
import eus.birt.proyecto.model.UsuarioEntity;

/**
 * Mapstruct para mapear las funcionalidades de inicio de sesión
 */
@Mapper
public abstract class UsuarioMapper {

	public static final UsuarioMapper MAPPER = Mappers.getMapper(UsuarioMapper.class);

	public abstract UsuarioEntity toEntity(UsuarioDTO dto);

}
