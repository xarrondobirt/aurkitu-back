package eus.birt.dam.aurkitu.objeto.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import eus.birt.dam.aurkitu.common.file.service.FileStorageService;
import eus.birt.dam.aurkitu.dto.ClaveValorDTO;
import eus.birt.dam.aurkitu.dto.ObjetoDTO;
import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.enums.EstadoObjetoEnum;
import eus.birt.dam.aurkitu.enums.FiltroBusquedaEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import eus.birt.dam.aurkitu.mapper.BuscarObjetoMapper;
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
import eus.birt.dam.aurkitu.payload.request.BuscarObjetoRequest;
import eus.birt.dam.aurkitu.payload.response.BuscarObjetoResponse;
import eus.birt.dam.aurkitu.payload.response.MensajeInfoResponse;
import eus.birt.dam.aurkitu.security.persistence.UsuarioRepository;
import eus.birt.dam.aurkitu.utils.Constantes;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
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
	private final FileStorageService fileStorageService;

	@Override
	@Transactional
	public MensajeResponse guardarObjeto(ObjetoDTO objetoDTO, SesionDTO sesion, MultipartFile foto,
			MultipartFile factura) {

//		log.info("OBJETO - SERVICE - GUARDAR");

		// Validaciones
		UsuarioEntity usuario = usuarioRepo.findById(sesion.getId())
				.orElseThrow(() -> new AurkituException(ErrorEnum.USER_NOT_FOUND));

		TipoObjetoEntity tipoObj = tipoObjetoRepo.findById(objetoDTO.getIdTipoObjeto())
				.orElseThrow(() -> new AurkituException(ErrorEnum.TIPO_OBJETO_NO_ENCONTRADO));

		ColorEntity color = colorRepo.findById(objetoDTO.getIdColor())
				.orElseThrow(() -> new AurkituException(ErrorEnum.COLOR_NO_ENCONTRADO));

		ObjetoEntity objeto = ObjetoMapper.MAPPER.toEntity(objetoDTO, color, tipoObj, usuario);

		// Subir ficheros
		if (foto != null) {
			String fotoUrl = fileStorageService.guardarFoto(foto);
			objeto.setFoto(fotoUrl);
		}

		if (factura != null) {
			String facturaUrl = fileStorageService.guardarDocumento(factura);
			objeto.setFactura(facturaUrl);
		}

		EstadoObjetoEntity estado = estadoObjetoRepo.findById(EstadoObjetoEnum.PERDIDO.ordinal() + 1)
				.orElseThrow(() -> new AurkituException(ErrorEnum.ESTADO_NO_ENCONTRADO));

		objeto.setEstado(estado);
		objetoRepo.save(objeto);

		return new MensajeInfoResponse(Constantes.OBJETO_GUARDADO);

	}

	@Override
	public List<ClaveValorDTO> obtenerTiposObjeto() {

//		log.info("OBJETO - SERVICE - OBTENER TIPOS OBJETO");

		List<TipoObjetoEntity> listaTiposObj = tipoObjetoRepo.findAll();
		return ClaveValorMapper.MAPPER.tipoObjetoToDTOList(listaTiposObj);
	}

	@Override
	public List<ClaveValorDTO> obtenerColores() {

//		log.info("OBJETO - SERVICE - OBTENER COLORES");

		List<ColorEntity> listaColor = colorRepo.findAll();
		return ClaveValorMapper.MAPPER.colorToDTOList(listaColor);
	}

	@Override
	public List<ClaveValorDTO> obtenerEstadosObjeto() {

//		log.info("OBJETO - SERVICE - OBTENER ESTADOS");

		List<EstadoObjetoEntity> listaEstados = estadoObjetoRepo.findAll();
		return ClaveValorMapper.MAPPER.estadoToDTOList(listaEstados);
	}

	@Override
	@Transactional
	public List<BuscarObjetoResponse> buscarObjetos(BuscarObjetoRequest filtros) {

//		log.info("OBJETO - SERVICE - BUSCAR");

		Specification<ObjetoEntity> spec = this.crearQuery(filtros);

		List<ObjetoEntity> objetos = objetoRepo.findAll(spec);

		return BuscarObjetoMapper.MAPPER.toResponseList(objetos);

	}

	/**
	 * Crea una especificación JPA para filtrar objetos basándose en múltiples criterios
	 * 
	 * @param filtros Objeto con todos los criterios de búsqueda aplicables
	 * @return Specification para la consulta JPA con los filtros aplicados
	 */
	private Specification<ObjetoEntity> crearQuery(BuscarObjetoRequest filtros) {

		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			// Filtro por ubicación y radio
			if (filtros.getUbicacion() != null) {

				int radio = filtros.getRadio() != null ? filtros.getRadio() : 0;

				// Usar función PostGIS ST_DWithin para búsqueda por radio
				Expression<Boolean> distancePredicate = cb.function("ST_DWithin", Boolean.class,
						root.get(FiltroBusquedaEnum.UBICACION.toString()),
						cb.function("ST_SetSRID", Point.class,
								cb.function("ST_MakePoint", Point.class,
										cb.literal(filtros.getUbicacion().getLongitud()),
										cb.literal(filtros.getUbicacion().getLatitud())),
								cb.literal(4326)),
						cb.literal(radio));
				predicates.add(cb.equal(distancePredicate, true));
			}

			// Filtro por tipo de objeto
			if (filtros.getTipo() != null) {
				predicates.add(
						cb.equal(root.get(FiltroBusquedaEnum.TIPO.toString()).get(FiltroBusquedaEnum.ID.toString()),
								filtros.getTipo().getId()));
			}

			// Filtro por color
			if (filtros.getColor() != null) {
				predicates.add(
						cb.equal(root.get(FiltroBusquedaEnum.COLOR.toString()).get(FiltroBusquedaEnum.ID.toString()),
								filtros.getColor().getId()));
			}

			// Filtro por estado
			if (filtros.getEstado() != null) {
				predicates.add(
						cb.equal(root.get(FiltroBusquedaEnum.ESTADO.toString()).get(FiltroBusquedaEnum.ID.toString()),
								filtros.getEstado().getId()));
			}

			// Filtro por fecha de pérdida (rango)
			if (filtros.getFechaDesde() != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get(FiltroBusquedaEnum.FECHA_PERDIDA.toString()),
						filtros.getFechaDesde()));
			}
			if (filtros.getFechaHasta() != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get(FiltroBusquedaEnum.FECHA_PERDIDA.toString()),
						filtros.getFechaHasta()));
			}

			// Filtro por descripción
			if (filtros.getDescripcion() != null && !filtros.getDescripcion().trim().isEmpty()) {
				String likePattern = "%" + filtros.getDescripcion().toLowerCase() + "%";
				predicates.add(cb.like(cb.lower(root.get(FiltroBusquedaEnum.DESCRIPCION.toString())), likePattern));
			}

			// Filtro por marca
			if (filtros.getMarca() != null && !filtros.getMarca().trim().isEmpty()) {
				String likePattern = "%" + filtros.getMarca().toLowerCase() + "%";
				predicates.add(cb.like(cb.lower(root.get(FiltroBusquedaEnum.MARCA.toString())), likePattern));
			}

			// Filtro por número de serie
			if (filtros.getNumSerie() != null && !filtros.getNumSerie().trim().isEmpty()) {
				predicates.add(cb.equal(root.get(FiltroBusquedaEnum.NUM_SERIE.toString()), filtros.getNumSerie()));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
