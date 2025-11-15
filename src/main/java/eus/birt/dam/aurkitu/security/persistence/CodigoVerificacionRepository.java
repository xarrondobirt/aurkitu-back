package eus.birt.dam.aurkitu.security.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.birt.dam.aurkitu.model.CodigoVerificacionEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;

/**
 * Repositorio para gestionar la entidad {@link CodigoVerificacionEntity}
 */
public interface CodigoVerificacionRepository extends JpaRepository<CodigoVerificacionEntity, Integer> {

	/**
	 * Busca un código de verificación por id de usuario
	 * 
	 * @param idUsuario Id del usuario
	 * @param codigo    Código de verificación enviado por el usuario
	 * @return CodigoVerificacionEntity con el código de verificación asociado al
	 *         usuario
	 */
	Optional<CodigoVerificacionEntity> findByUsuarioIdAndCodigo(Integer idUsuario, String codigo);

	/**
	 * Borrar el código de verificación de un usuario
	 * 
	 * @param usuario Usuario que tiene el código
	 */
	void deleteByUsuario(UsuarioEntity usuario);
}
