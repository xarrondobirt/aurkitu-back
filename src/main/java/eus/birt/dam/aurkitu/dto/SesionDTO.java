package eus.birt.dam.aurkitu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la sesión de un usuario
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class SesionDTO {

	private Integer id;
	private String username;
}
