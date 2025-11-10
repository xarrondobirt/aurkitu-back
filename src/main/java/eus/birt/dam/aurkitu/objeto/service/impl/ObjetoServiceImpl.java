package eus.birt.dam.aurkitu.objeto.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import eus.birt.dam.aurkitu.dto.ClaveValorDTO;
import eus.birt.dam.aurkitu.dto.ObjetoDTO;
import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.enums.EstadoObjetoEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import eus.birt.dam.aurkitu.mapper.ClaveValorMapper;
import eus.birt.dam.aurkitu.mapper.ObjetoMapper;
import eus.birt.dam.aurkitu.model.ColorEntity;
import eus.birt.dam.aurkitu.model.EstadoObjetoEntity;
import eus.birt.dam.aurkitu.model.ObjetoEntity;
import eus.birt.dam.aurkitu.model.TipoObjetoEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;
import eus.birt.dam.aurkitu.objeto.persistence.ColorRepository;
import eus.birt.dam.aurkitu.objeto.persistence.EstadoObjetoRepository;
import eus.birt.dam.aurkitu.objeto.persistence.ObjetoRepository;
import eus.birt.dam.aurkitu.objeto.persistence.TipoObjetoRepository;
import eus.birt.dam.aurkitu.objeto.service.ObjetoService;
import eus.birt.dam.aurkitu.payload.response.MensajeResponse;
import eus.birt.dam.aurkitu.security.persistence.UsuarioRepository;
import eus.birt.dam.aurkitu.utils.Constantes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio que implementa la interfaz {@link ObjetoService}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ObjetoServiceImpl implements ObjetoService {

	private final ObjetoRepository objetoRepo;
	private final TipoObjetoRepository tipoObjetoRepo;
	private final ColorRepository colorRepo;
	private final UsuarioRepository usuarioRepo;
	private final EstadoObjetoRepository estadoObjetoRepo;

	@Override
	@Transactional
	public MensajeResponse guardarObjeto(ObjetoDTO objetoDTO, Integer idUsuario) {

		log.info("OBJETO - SERVICE - GUARDAR");

		// Validaciones
		UsuarioEntity usuario = usuarioRepo.findById(idUsuario)
				.orElseThrow(() -> new AurkituException(ErrorEnum.USER_NOT_FOUND));

		TipoObjetoEntity tipoObj = tipoObjetoRepo.findById(objetoDTO.getIdTipoObjeto())
				.orElseThrow(() -> new AurkituException(ErrorEnum.TIPO_OBJETO_NO_ENCONTRADO));

		ColorEntity color = colorRepo.findById(objetoDTO.getIdColor())
				.orElseThrow(() -> new AurkituException(ErrorEnum.COLOR_NO_ENCONTRADO));

		ObjetoEntity objeto = ObjetoMapper.MAPPER.toEntity(objetoDTO, color, tipoObj, usuario);

		EstadoObjetoEntity estado = estadoObjetoRepo.findById(EstadoObjetoEnum.PERDIDO.ordinal())
				.orElseThrow(() -> new AurkituException(ErrorEnum.ESTADO_NO_ENCONTRADO));

		objeto.setEstado(estado);
		objetoRepo.save(objeto);

		return new MensajeResponse(Constantes.OBJETO_GUARDADO);

	}

	@Override
	public List<ClaveValorDTO> obtenerTiposObjeto() {

		log.info("OBJETO - SERVICE - OBTENER TIPOS OBJETO");

		List<TipoObjetoEntity> listaTiposObj = tipoObjetoRepo.findAll();
		return ClaveValorMapper.MAPPER.tipoObjetoToDTOList(listaTiposObj);
	}

	@Override
	public List<ClaveValorDTO> obtenerColores() {

		log.info("OBJETO - SERVICE - OBTENER COLORES");

		List<ColorEntity> listaColor = colorRepo.findAll();
		return ClaveValorMapper.MAPPER.colorToDTOList(listaColor);
	}
}
