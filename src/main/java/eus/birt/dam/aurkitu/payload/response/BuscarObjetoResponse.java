package eus.birt.dam.aurkitu.payload.response;

import java.time.Instant;

import eus.birt.dam.aurkitu.dto.ClaveValorDTO;
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
	private String numSerie;
	private UbicacionDTO ubicacion;
	private Integer radio;
	private Instant fechaPerdida;
	private String foto;
	private String factura;
	private ClaveValorDTO tipoObjeto;
	private ClaveValorDTO color;
	private ClaveValorDTO estado;
	private Instant createDate;
	private Instant lastUpdateDate;

}
