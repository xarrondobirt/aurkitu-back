package eus.birt.dam.aurkitu.payload.response;

import java.time.Instant;

import eus.birt.dam.aurkitu.dto.SesionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase para serializar las conversaciones de un usuario
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Respuesta con información de una conversación entre usuarios")
public final class ConversacionResponse {

	@Schema(description = "ID único de la conversación")
	private Integer id;

	@Schema(description = "Información del otro participante en la conversación")
	private SesionDTO participante;

	@Schema(description = "ID del objeto relacionado con la conversación")
	private Integer idObjeto;

	@Schema(description = "Tipo del objeto relacionado con la conversación")
	private String tipoObjeto;

	@Schema(description = "Fecha y hora de creación de la conversación")
	private Instant createDate;

	@Schema(description = "Fecha y hora del último mensaje en la conversación")
	private Instant lastUpdateDate;

	@Schema(description = "Indica si hay mensajes nuevos sin leer en la conversación")
	private boolean mensajesSinLeer;

	@Schema(description = "Muestra el botón de cerrar caso")
	private boolean btnCerrarCaso;
}
