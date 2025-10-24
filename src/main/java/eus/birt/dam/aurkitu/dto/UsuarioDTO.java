package eus.birt.proyecto.dto;

import eus.birt.proyecto.model.UsuarioEntity;
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
public final class UsuarioDTO {

	@NotBlank(message = "Username es obligatorio")
	@Size(min = 3, max = 50, message = "Username debe tener entre 3 y 50 caracteres")
	private String username;

	@NotBlank(message = "Email es obligatorio")
	@Email(message = "Formato de email inválido")
	private String email;

	@NotBlank(message = "Contraseña es obligatoria")
	@Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
	private String password;
}
