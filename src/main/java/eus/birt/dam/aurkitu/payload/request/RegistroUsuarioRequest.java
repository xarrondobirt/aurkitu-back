package eus.birt.proyecto.payload.request;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
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
public final class RegistroUsuarioRequest {

	@Nonnull
	private Integer idUsuario;

	@NotBlank
	private String codigoVerificacion;

}
