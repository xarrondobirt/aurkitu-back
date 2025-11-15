package eus.birt.dam.aurkitu.objeto.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.birt.dam.aurkitu.model.TipoObjetoEntity;

/**
 * Repositorio para gestionar entidades de tipoObjetos
 */
public interface TipoObjetoRepository extends JpaRepository<TipoObjetoEntity, Integer> {

}
