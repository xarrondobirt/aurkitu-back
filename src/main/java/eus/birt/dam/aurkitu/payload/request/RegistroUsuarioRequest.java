package eus.birt.dam.aurkitu.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que se usa como request a la hora de verificar el registro de un
 * usuario
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para verificar registro de usuario")
public final class RegistroUsuarioRequest {

	@NotNull
	@Min(value = 1, message = "El ID de usuario debe ser mayor a 0")
	@Schema(description = "ID del usuario que se está verificando")
	private Integer idUsuario;

	@NotBlank
	@Size(min = 6, max = 6)
	@Schema(description = "Código de verificación de 6 dígitos enviado por email")
	private String codigoVerificacion;

}
