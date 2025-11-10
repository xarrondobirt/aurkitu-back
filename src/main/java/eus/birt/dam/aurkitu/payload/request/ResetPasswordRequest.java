package eus.birt.dam.aurkitu.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que se usa como request para solicitar recuperar la contraseña
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class ResetPasswordRequest {

	@Min(value = 1, message = "El ID de usuario debe ser mayor a 0")
	@NotNull
	private Integer idUsuario;

	@NotBlank
	private String nuevaPassword;

	@NotBlank
	private String repitePassword;

	@NotNull
	@Size(min = 6, max = 6)
	private String codVerificacion;

}
