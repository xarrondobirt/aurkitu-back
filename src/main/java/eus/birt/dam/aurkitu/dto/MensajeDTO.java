package eus.birt.dam.aurkitu.dto;

import java.time.Instant;

import eus.birt.dam.aurkitu.model.MensajeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la entidad {@link MensajeEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class MensajeDTO {

	private Long id;
	private SesionDTO remitente;
	private String contenido;
	private boolean leido;
	private Instant createDate;
}
