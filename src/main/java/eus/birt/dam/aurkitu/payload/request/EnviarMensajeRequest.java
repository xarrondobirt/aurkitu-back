package eus.birt.dam.aurkitu.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que se usa como request para enviar mensajes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class EnviarMensajeRequest {

	@NotNull
	private Integer idDestinatario;

	@NotNull
	private Integer idObjeto;

	@NotBlank
	private String contenido;

}
