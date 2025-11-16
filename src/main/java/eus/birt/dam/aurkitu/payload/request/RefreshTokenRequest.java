package eus.birt.dam.aurkitu.payload.request;

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
public final class RefreshTokenRequest {

	@NotBlank
	private String token;

}
