package eus.birt.dam.aurkitu.objeto.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import eus.birt.dam.aurkitu.common.file.service.FileStorageService;
import eus.birt.dam.aurkitu.dto.ClaveValorDTO;
import eus.birt.dam.aurkitu.dto.MensajeDTO;
import eus.birt.dam.aurkitu.dto.ObjetoDTO;
import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.enums.EstadoObjetoEnum;
import eus.birt.dam.aurkitu.enums.FiltroBusquedaEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import eus.birt.dam.aurkitu.mapper.BuscarObjetoMapper;
import eus.birt.dam.aurkitu.mapper.ClaveValorMapper;
import eus.birt.dam.aurkitu.mapper.MensajeMapper;
import eus.birt.dam.aurkitu.mapper.ObjetoMapper;
import eus.birt.dam.aurkitu.mensajes.persistence.MensajeRepository;
import eus.birt.dam.aurkitu.mensajes.service.MensajeService;
import eus.birt.dam.aurkitu.model.ColorEntity;
import eus.birt.dam.aurkitu.model.ConversacionEntity;
import eus.birt.dam.aurkitu.model.EstadoObjetoEntity;
import eus.birt.dam.aurkitu.model.MensajeEntity;
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
import eus.birt.dam.aurkitu.payload.response.ConversacionDetalleResponse;
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
	private final MensajeService mensajeService;
	private final MensajeRepository mensajeRepo;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public MensajeInfoResponse guardarObjeto(ObjetoDTO objetoDTO, SesionDTO sesion, MultipartFile foto,
			MultipartFile factura) {

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

		List<TipoObjetoEntity> listaTiposObj = tipoObjetoRepo.findAll();
		return ClaveValorMapper.MAPPER.tipoObjetoToDTOList(listaTiposObj);
	}

	@Override
	public List<ClaveValorDTO> obtenerColores() {

		List<ColorEntity> listaColor = colorRepo.findAll();
		return ClaveValorMapper.MAPPER.colorToDTOList(listaColor);
	}

	@Override
	public List<ClaveValorDTO> obtenerEstadosObjeto() {

		List<EstadoObjetoEntity> listaEstados = estadoObjetoRepo.findAll();
		return ClaveValorMapper.MAPPER.estadoToDTOList(listaEstados);
	}

	@Override
	public List<BuscarObjetoResponse> buscarObjetos(BuscarObjetoRequest filtros, SesionDTO sesion) {

		Specification<ObjetoEntity> spec = this.crearQuery(filtros);

		List<ObjetoEntity> objetos = objetoRepo.findAll(spec);

		return BuscarObjetoMapper.MAPPER.toResponseList(objetos, sesion);

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

			// Filtro fijo por estado: Siempre mostrar solo objetos PERDIDOS
			predicates
					.add(cb.equal(root.get(FiltroBusquedaEnum.ESTADO.toString()).get(FiltroBusquedaEnum.ID.toString()),
							EstadoObjetoEnum.PERDIDO.ordinal() + 1));

			// Filtro por ubicación y radio
			int radio = filtros.getRadio() != null ? filtros.getRadio() : 0;

			// Conversión a grados
			double radioGrados = radio / 111000.0;

			Expression<Point> puntoBusqueda = cb.function("ST_SetSRID", Point.class,
					cb.function("ST_MakePoint", Point.class, cb.literal(filtros.getUbicacion().getLongitud()),
							cb.literal(filtros.getUbicacion().getLatitud())),
					cb.literal(4326));

			// Usar función PostGIS ST_DWithin para búsqueda por radio
			Expression<Boolean> distancePredicate = cb.function("ST_DWithin", Boolean.class,
					root.get(FiltroBusquedaEnum.UBICACION.toString()), puntoBusqueda, cb.literal(radioGrados));
//			predicates.add(cb.equal(distancePredicate, true));
			predicates.add(cb.isTrue(distancePredicate));

			// Orden por distancia (ST_Distance)
			Expression<Double> distancia = cb.function("ST_Distance", Double.class,
					root.get(FiltroBusquedaEnum.UBICACION.toString()), puntoBusqueda);

			query.orderBy(cb.asc(distancia));

			// Filtro por tipo de objeto
			predicates.add(cb.equal(root.get(FiltroBusquedaEnum.TIPO.toString()).get(FiltroBusquedaEnum.ID.toString()),
					filtros.getTipo().getId()));

			Instant inicioDia = filtros.getFecha().truncatedTo(ChronoUnit.DAYS);

			Instant finDia = Instant.now().truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS).minus(1,
					ChronoUnit.MILLIS);
			predicates.add(cb.between(root.get(FiltroBusquedaEnum.FECHA.toString()), inicioDia, finDia));

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public ConversacionDetalleResponse verChat(SesionDTO sesion, Integer idUsuario, Integer idObjeto) {

		UsuarioEntity remitente = usuarioRepo.findById(sesion.getId())
				.orElseThrow(() -> new AurkituException(ErrorEnum.USER_NOT_FOUND));

		UsuarioEntity destinatario = usuarioRepo.findById(idUsuario)
				.orElseThrow(() -> new AurkituException(ErrorEnum.USER_NOT_FOUND));

		ObjetoEntity objeto = objetoRepo.findById(idObjeto)
				.orElseThrow(() -> new AurkituException(ErrorEnum.OBJETO_NO_ENCONTRADO));

		ConversacionEntity conversacion = mensajeService.obtenerCrearConversacion(remitente, destinatario, objeto);

		// Marcar como leídos solo los mensajes donde el usuario no es el remitente
		List<MensajeEntity> mensajes = conversacion.getMensajes().stream()
				.filter(m -> !m.isLeido() && !m.getRemitente().getId().equals(sesion.getId())).toList();

		mensajes.forEach(m -> m.setLeido(true));
		mensajeRepo.saveAll(mensajes);

		List<MensajeDTO> listaMensajes = MensajeMapper.MAPPER.toListDTO(conversacion.getMensajes(), sesion);

		return ConversacionDetalleResponse.builder().idConversacion(conversacion.getId()).mensajes(listaMensajes)
				.build();

	}
}
