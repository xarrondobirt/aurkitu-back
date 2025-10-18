package eus.birt.proyecto.security.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.birt.proyecto.model.UsuarioEntity;

/**
 * Repositorio para gestionar entidades de usuarios
 */
public interface UserRepository extends JpaRepository<UsuarioEntity, Integer> {

	/**
	 * Busca un UsuarioEntity por su nombre de usuario.
	 *
	 * @param username El nombre de usuario a buscar.
	 * @return UsuarioEntity si se encuentra
	 */
//	Optional<UsuarioEntity> findByUsername(String username);

	boolean existsByEmail(String email);
}
