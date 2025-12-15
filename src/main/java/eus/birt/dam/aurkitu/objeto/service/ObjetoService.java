package eus.birt.dam.aurkitu.objeto.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import eus.birt.dam.aurkitu.dto.ClaveValorDTO;
import eus.birt.dam.aurkitu.dto.ObjetoDTO;
import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.payload.request.BuscarObjetoRequest;
import eus.birt.dam.aurkitu.payload.response.BuscarObjetoResponse;
import eus.birt.dam.aurkitu.payload.response.MensajeInfoResponse;

/**
 * Interfaz que define las operaciones relacionadas con la gestión de objetos
 * 
 */
public interface ObjetoService {

	/**
	 * Guarda un objeto en el sistema
	 * 
	 * @param objetoDTO DTO con los datos del objeto a guardar
	 * @param idUsuario ID del usuario que realiza la operación
	 * @param foto      fichero de la foto
	 * @param factura   fichero de la factura
	 * @return Respuesta con el resultado de la operación
	 */
	MensajeInfoResponse guardarObjeto(ObjetoDTO objetoDTO, SesionDTO sesion, MultipartFile foto, MultipartFile factura);

	/**
	 * Obtiene la lista de tipos de objeto disponibles
	 * 
	 * @return Lista de DTOs clave-valor con los tipos de objeto
	 */
	List<ClaveValorDTO> obtenerTiposObjeto();

	/**
	 * Obtiene la lista de colores disponibles para objetos
	 * 
	 * @return Lista de DTOs clave-valor con los colores
	 */
	List<ClaveValorDTO> obtenerColores();

	/**
	 * Obtiene la lista de estados disponibles para objetos
	 * 
	 * @return Lista de DTOs clave-valor con los estados de objeto
	 */
	List<ClaveValorDTO> obtenerEstadosObjeto();

	/**
	 * Busca objetos aplicando múltiples filtros
	 * 
	 * @param filtros Objeto con todos los criterios de búsqueda aplicables
	 * @return Lista de objetos que coinciden con los filtros
	 */
	List<BuscarObjetoResponse> buscarObjetos(BuscarObjetoRequest filtros);

}
