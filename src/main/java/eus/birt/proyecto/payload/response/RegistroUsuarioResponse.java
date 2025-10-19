package eus.birt.proyecto.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase para serializar la información del usuario registrado sin verificar
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class RegistroUsuarioResponse {
	private Integer id;
	private String mensaje;

}
