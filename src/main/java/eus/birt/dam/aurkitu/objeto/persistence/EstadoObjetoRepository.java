package eus.birt.dam.aurkitu.objeto.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.birt.dam.aurkitu.model.EstadoObjetoEntity;

/**
 * Repositorio para gestionar entidades de estado objeto
 */
public interface EstadoObjetoRepository extends JpaRepository<EstadoObjetoEntity, Integer> {

}
