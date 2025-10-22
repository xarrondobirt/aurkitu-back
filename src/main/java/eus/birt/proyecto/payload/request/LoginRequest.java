package eus.birt.proyecto.payload.request;

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
public final class LoginRequest {

	@NotBlank
	private String username;

	@NotBlank
	private String password;

}
