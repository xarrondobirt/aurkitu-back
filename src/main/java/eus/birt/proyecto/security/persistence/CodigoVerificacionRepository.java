package eus.birt.proyecto.security.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.birt.proyecto.model.CodigoVerificacionEntity;

/**
 * Repositorio para gestionar la entidad {@link CodigoVerificacionEntity}
 */
public interface CodigoVerificacionRepository extends JpaRepository<CodigoVerificacionEntity, Integer> {

}
