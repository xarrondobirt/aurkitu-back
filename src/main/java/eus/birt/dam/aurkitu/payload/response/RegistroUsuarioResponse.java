package eus.birt.dam.aurkitu.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase para serializar la información del usuario registrado sin verificar
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta tras registro de usuario (pendiente de verificación)")
public final class RegistroUsuarioResponse {

	@Schema(description = "ID asignado al nuevo usuario registrado")
	private Integer id;

	@Schema(description = "Mensaje informativo sobre el estado del registro")
	private String mensaje;

}
