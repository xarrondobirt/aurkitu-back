package eus.birt.proyecto.security.persistence;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.birt.proyecto.model.RefreshTokenEntity;

/**
 * Repositorio para {@link RefreshTokenEntity}
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {

	/**
	 * Busca un refreshtoken por el id usuario
	 * 
	 * @param idUser Id del usuario
	 * @return RefreshToken del usuario
	 */
	Set<RefreshTokenEntity> findByUsuarioId(Integer idUser);

}
