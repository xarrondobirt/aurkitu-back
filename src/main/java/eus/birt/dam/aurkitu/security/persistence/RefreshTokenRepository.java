package eus.birt.dam.aurkitu.security.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.birt.dam.aurkitu.model.RefreshTokenEntity;

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
	Optional<RefreshTokenEntity> findByUsuarioId(Integer idUser);

}
