package eus.birt.dam.aurkitu.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que se usa como request para enviar mensajes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para enviar un mensaje a otro usuario")
public final class EnviarMensajeRequest {

	@NotNull
	@Schema(description = "ID del usuario destinatario del mensaje")
	private Integer idDestinatario;

	@NotNull
	@Schema(description = "ID del objeto relacionado con la conversación")
	private Integer idObjeto;

	@NotBlank
	@Schema(description = "Contenido del mensaje a enviar")
	private String contenido;

}
