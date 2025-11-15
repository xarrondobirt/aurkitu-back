package eus.birt.dam.aurkitu.objeto.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.birt.dam.aurkitu.model.ObjetoEntity;

/**
 * Repositorio para gestionar entidades de objetos
 */
public interface ObjetoRepository extends JpaRepository<ObjetoEntity, Integer> {

}
