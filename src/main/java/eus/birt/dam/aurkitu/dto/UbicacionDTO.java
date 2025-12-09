package eus.birt.dam.aurkitu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO para coordenadas geográficas (latitud/longitud)")
public final class UbicacionDTO {

	@NotNull(message = "La latitud es obligatoria")
	@Schema(description = "Coordenada de latitud")
	private Double latitud;

	@NotNull(message = "La longitud es obligatoria")
	@Schema(description = "Coordenada de longitud")
	private Double longitud;

	@NotNull(message = "La latitud es obligatoria")
	private Double latitud;

}
