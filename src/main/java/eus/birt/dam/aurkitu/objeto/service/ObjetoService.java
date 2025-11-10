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

	MensajeResponse guardarObjeto(ObjetoDTO objetoDTO, Integer idUsuario);

	List<ClaveValorDTO> obtenerTiposObjeto();

	List<ClaveValorDTO> obtenerColores();

}
