package eus.birt.dam.aurkitu.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que se usa como request para solicitar recuperar la contraseña
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para recuperar contraseña")
public final class RecuperarPasswordRequest {

	@NotBlank
	@Email
	@Schema(description = "Email del usuario que solicita recuperación")
	private String email;

}
