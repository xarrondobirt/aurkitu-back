package eus.birt.dam.aurkitu.objeto.service;

import java.util.List;

import eus.birt.dam.aurkitu.dto.ClaveValorDTO;
import eus.birt.dam.aurkitu.dto.ObjetoDTO;
import eus.birt.dam.aurkitu.payload.response.MensajeResponse;

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
	 * @return Respuesta con el resultado de la operación
	 */
	MensajeResponse guardarObjeto(ObjetoDTO objetoDTO, Integer idUsuario);

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

}
