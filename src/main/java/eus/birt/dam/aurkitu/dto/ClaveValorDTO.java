package eus.birt.dam.aurkitu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO genérico para objetos clave-valor
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class ClaveValorDTO {

	private Integer id;
	private String descripcion;

}
