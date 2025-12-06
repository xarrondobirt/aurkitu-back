package eus.birt.dam.aurkitu.mensajes.persistence;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.birt.dam.aurkitu.model.ConversacionEntity;
import eus.birt.dam.aurkitu.model.ObjetoEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;

/**
 * Repositorio para gestionar entidades de conversacion
 */
public interface ConversacionRepository extends JpaRepository<ConversacionEntity, Integer> {

	Set<ConversacionEntity> findByParticipante1IdOrParticipante2Id(Integer idPart1, Integer idPart2);

	Optional<ConversacionEntity> findByParticipante1AndParticipante2AndObjeto(UsuarioEntity usuario1,
			UsuarioEntity usuario2, ObjetoEntity obj);

}
