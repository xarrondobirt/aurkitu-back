package eus.birt.dam.aurkitu.dto;

import eus.birt.dam.aurkitu.model.UsuarioEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa {@link UsuarioEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de usuario para registro")
public final class UsuarioDTO {

	@NotBlank(message = "Username es obligatorio")
	@Size(min = 3, max = 50, message = "Username debe tener entre 3 y 50 caracteres")
	@Schema(description = "Nombre de usuario único para identificación")
	private String username;

	@NotBlank(message = "Email es obligatorio")
	@Email(message = "Formato de email inválido")
	@Schema(description = "Email del usuario para contacto y verificación")
	private String email;

	@NotBlank(message = "Contraseña es obligatoria")
	@Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
	@Schema(description = "Contraseña del usuario para autenticación")
	private String password;
}
