package eus.birt.proyecto.payload.response;

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
public final class LoginResponse {

	@NotBlank
	private String accessToken;

}
