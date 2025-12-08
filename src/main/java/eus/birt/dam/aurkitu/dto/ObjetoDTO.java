package eus.birt.dam.aurkitu.dto;

import java.time.Instant;

import eus.birt.dam.aurkitu.model.ObjetoEntity;
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
public final class ObjetoDTO {

	@Valid
	@NotNull(message = "La ubicación es obligatoria")
	private UbicacionDTO ubicacion;

	private Integer radio;

	@NotNull(message = "Tipo de objeto obligatorio")
	private Integer idTipoObjeto;

	@NotBlank(message = "La descripción es obligatoria")
	private String descripcion;

	private String marca;

	private String numSerie;

	@NotNull(message = "El color es obligatorio")
	private Integer idColor;

//	private String foto;

//	private String factura;

	@NotNull(message = "La fecha de pérdida es obligatoria")
	private Instant fechaPerdida;

}
