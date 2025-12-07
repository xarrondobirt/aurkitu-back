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

	/**
	 * Busca conversaciones donde un usuario específico sea participante (como participante 1 o 2)
	 * 
	 * @param idPart1 identificador del primer participante
	 * @param idPart2 identificador del segundo participante
	 * @return conjunto de conversaciones donde el usuario es participante
	 */
	Set<ConversacionEntity> findByParticipante1IdOrParticipante2Id(Integer idPart1, Integer idPart2);

	/**
	 * Busca una conversación específica entre dos usuarios sobre un objeto determinado
	 * 
	 * @param usuario1 primer usuario participante
	 * @param usuario2 segundo usuario participante
	 * @param obj      objeto sobre el que trata la conversación
	 * @return conversación encontrada o Optional vacío si no existe
	 */
	Optional<ConversacionEntity> findByParticipante1AndParticipante2AndObjeto(UsuarioEntity usuario1,
			UsuarioEntity usuario2, ObjetoEntity obj);

}
