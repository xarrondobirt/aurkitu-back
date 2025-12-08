package eus.birt.dam.aurkitu.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que se usa como request para refresh token
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para refrescar token de acceso")
public final class RefreshTokenRequest {

	@NotBlank
	@Schema(description = "Refresh token válido para obtener nuevo access token")
	private String token;

}
