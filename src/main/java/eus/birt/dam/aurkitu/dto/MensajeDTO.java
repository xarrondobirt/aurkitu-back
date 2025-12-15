package eus.birt.dam.aurkitu.dto;

import java.time.Instant;

import eus.birt.dam.aurkitu.model.MensajeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la entidad {@link MensajeEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO que representa un mensaje en una conversación")
public final class MensajeDTO {

	@Schema(description = "ID único del mensaje")
	private Long id;

	@Schema(description = "Información del usuario que envió el mensaje")
	private SesionDTO remitente;

	@Schema(description = "Texto del mensaje")
	private String contenido;

	@Schema(description = "Indica si el mensaje ha sido leído por el destinatario")
	private boolean leido;

	@Schema(description = "Fecha y hora de creación del mensaje")
	private Instant createDate;

	@Schema(description = "Fecha y hora de la última modificación del mensaje")
	private Instant lastUpdateDate;
}
