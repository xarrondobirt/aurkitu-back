package eus.birt.dam.aurkitu.dto;

import java.time.Instant;

import eus.birt.dam.aurkitu.model.ObjetoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa {@link ObjetoEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para transporte de objetos perdidos")
public final class ObjetoDTO {

	@Valid
	@NotNull(message = "La ubicación es obligatoria")
	@Schema(description = "Ubicación donde se perdió el objeto")
	private UbicacionDTO ubicacion;

	@Schema(description = "Radio de búsqueda en metros")
	private Integer radio;

	@NotNull(message = "Tipo de objeto obligatorio")
	@Schema(description = "ID del tipo de objeto")
	private Integer idTipoObjeto;

	@NotBlank(message = "La descripción es obligatoria")
	@Schema(description = "Descripción detallada del objeto")
	private String descripcion;

	@Schema(description = "Marca del objeto")
	private String marca;

	@Schema(description = "Número de serie")
	private String numSerie;

	@NotNull(message = "El color es obligatorio")
	@Schema(description = "ID del color predominante")
	private Integer idColor;

	private String foto;

	private String factura;

	@NotNull(message = "La fecha de pérdida es obligatoria")
	@Schema(description = "Fecha y hora en que se perdió el objeto")
	private Instant fechaPerdida;

}
