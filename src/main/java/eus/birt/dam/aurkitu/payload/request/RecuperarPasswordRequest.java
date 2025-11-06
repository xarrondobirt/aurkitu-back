package eus.birt.dam.aurkitu.payload.request;

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
public final class RecuperarPasswordRequest {

	@NotBlank
	@Email
	private String email;

}
