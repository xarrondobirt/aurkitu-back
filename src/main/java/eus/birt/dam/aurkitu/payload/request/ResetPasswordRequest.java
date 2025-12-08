package eus.birt.dam.aurkitu.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
@Schema(description = "Solicitud para restablecer contraseña con código de verificación")
public final class ResetPasswordRequest {

	@Email
	@NotBlank
	@Schema(description = "Email del usuario que restablece la contraseña")
	private String email;

	@NotBlank
	@Schema(description = "Nueva contraseña del usuario")
	private String nuevaPassword;

	@NotBlank
	@Schema(description = "Confirmación de la nueva contraseña")
	private String repitePassword;

	@NotNull
	@Size(min = 6, max = 6)
	@Schema(description = "Código de verificación de 6 dígitos")
	private String codVerificacion;

}
