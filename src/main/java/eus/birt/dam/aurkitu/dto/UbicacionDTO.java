package eus.birt.dam.aurkitu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la geolocalización
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class UbicacionDTO {

	@NotNull(message = "La latitud es obligatoria")
	private Double latitud;

	@NotNull(message = "La longitud es obligatoria")
	private Double longitud;

}
