package eus.birt.dam.aurkitu.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que se usa como request al hacer login
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Credenciales para iniciar sesión")
public final class LoginRequest {

	@NotBlank
	@Schema(description = "Nombre de usuario para autenticación")
	private String username;

	@NotBlank
	@Schema(description = "Contraseña del usuario")
	private String password;

}
