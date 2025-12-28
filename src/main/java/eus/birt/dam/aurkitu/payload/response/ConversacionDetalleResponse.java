package eus.birt.dam.aurkitu.payload.response;

import java.util.ArrayList;
import java.util.List;

import eus.birt.dam.aurkitu.dto.MensajeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase para serializar las conversaciones con los mensajes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Respuesta con información de una conversación entre usuarios detallada")
public final class ConversacionDetalleResponse {

	@Schema(description = "ID único de la conversación")
	private Integer idConversacion;

	@Schema(description = "Información del otro participante en la conversación")
	@Builder.Default
	private List<MensajeDTO> mensajes = new ArrayList<>();

}
