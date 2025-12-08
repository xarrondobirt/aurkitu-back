package eus.birt.dam.aurkitu.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que se usa como respuesta al hacer login
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta con tokens de autenticación tras login exitoso")
public final class LoginResponse {

	@NotBlank
	@Schema(description = "Token JWT para acceder a endpoints protegidos")
	private String accessToken;

	@NotBlank
	@Schema(description = "Token para renovar el access token cuando expire")
	private String refreshToken;
}
