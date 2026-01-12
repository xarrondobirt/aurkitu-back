package eus.birt.dam.aurkitu.payload.response;

import java.time.Instant;

import eus.birt.dam.aurkitu.dto.ClaveValorDTO;
import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.dto.UbicacionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que se usa como response para la búsqueda de objetos
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class BuscarObjetoResponse {

	private Integer id;
	private String descripcion;
	private String marca;
	private String serie;
	private UbicacionDTO ubicacion;
	private Integer radio;
	private Instant fecha;
	private String foto;
	private String factura;
	private ClaveValorDTO tipo;
	private ClaveValorDTO color;
	private ClaveValorDTO estado;
	private Instant createDate;
	private Instant lastUpdateDate;
	private SesionDTO usuario;
	private boolean mostrarChat;

}
