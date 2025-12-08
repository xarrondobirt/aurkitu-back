package eus.birt.dam.aurkitu.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase para serializar los mensajes informativos para el usuario
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta genérica con mensaje informativo para el usuario")
public final class MensajeInfoResponse {

	@Schema(description = "Mensaje descriptivo de la operación realizada")
	private String mensaje;
}
