package eus.birt.dam.aurkitu.objeto.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.birt.dam.aurkitu.model.ColorEntity;

/**
 * Repositorio para gestionar entidades de color
 */
public interface ColorRepository extends JpaRepository<ColorEntity, Integer> {

}
