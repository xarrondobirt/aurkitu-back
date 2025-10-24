package eus.birt.dam.aurkitu.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase para serializar los mensajes informativos para el usuario
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class MensajeResponse {

	private String mensaje;
}
