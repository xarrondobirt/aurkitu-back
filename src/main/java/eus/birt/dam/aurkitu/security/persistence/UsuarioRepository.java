package eus.birt.dam.aurkitu.security.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.birt.dam.aurkitu.model.UsuarioEntity;

/**
 * Repositorio para gestionar entidades de usuarios
 */
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

	/**
	 * Comprueba si existe un email
	 * 
	 * @param email Email que se va a buscar
	 * @return true si existe el email, false en caso contrario
	 */
	boolean existsByEmail(String email);

	/**
	 * Comprueba si existe un username
	 * 
	 * @param username Nombre de usuario que se va a buscar
	 * @return true si existe el username, false en caso contrario
	 */
	boolean existsByUsername(String username);

	/**
	 * Busca un usuario por su username
	 * 
	 * @param username Nombre del usuario
	 * @return Un usuario si existe
	 */
	Optional<UsuarioEntity> findByUsername(String username);
}
