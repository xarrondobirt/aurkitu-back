package eus.birt.dam.aurkitu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO genérico para objetos clave-valor
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO genérico para listas de opciones (ID y descripción)")
public final class ClaveValorDTO {

	@Schema(description = "Identificador único del elemento")
	private Integer id;

	@Schema(description = "Descripción o nombre del elemento")
	private String descripcion;

}
