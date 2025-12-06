package eus.birt.dam.aurkitu.mensajes.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.birt.dam.aurkitu.model.MensajeEntity;

/**
 * Repositorio para gestionar entidades de mensajes
 */
public interface MensajeRepository extends JpaRepository<MensajeEntity, Integer> {

}
