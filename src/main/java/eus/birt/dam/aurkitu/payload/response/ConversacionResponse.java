package eus.birt.dam.aurkitu.payload.response;

import java.time.Instant;

import eus.birt.dam.aurkitu.dto.SesionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase para serializar las conversaciones de un usuario
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class ConversacionResponse {

	private Integer id;
	private SesionDTO participante;
	private Integer idObjeto;
//	private List<MensajeDTO> mensajes = new ArrayList<>();
	private Instant createDate;
	private Instant lastUpdateDate;
	private boolean mensajesSinLeer;
}
