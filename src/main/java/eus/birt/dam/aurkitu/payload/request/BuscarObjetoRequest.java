package eus.birt.dam.aurkitu.payload.request;

import java.time.Instant;

import eus.birt.dam.aurkitu.dto.ClaveValorDTO;
import eus.birt.dam.aurkitu.dto.UbicacionDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que se usa como request para la búsqueda de objetos
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class BuscarObjetoRequest {

	@NotNull(message = "La ubicación es obligatoria")
	private UbicacionDTO ubicacion;

	private Integer radio;

	@NotNull(message = "El tipo de objeto es obligatorio")
	private ClaveValorDTO tipo;
	// private String descripcion;
	// private String marca;
	// private String serie;
	// private ClaveValorDTO color;
	private Instant fecha;
}
