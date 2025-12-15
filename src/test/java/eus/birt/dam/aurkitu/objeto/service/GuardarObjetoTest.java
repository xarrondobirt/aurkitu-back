package eus.birt.dam.aurkitu.objeto.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import eus.birt.dam.aurkitu.dto.ObjetoDTO;
import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.enums.EstadoObjetoEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import eus.birt.dam.aurkitu.model.ColorEntity;
import eus.birt.dam.aurkitu.model.EstadoObjetoEntity;
import eus.birt.dam.aurkitu.model.ObjetoEntity;
import eus.birt.dam.aurkitu.model.TipoObjetoEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;
import eus.birt.dam.aurkitu.objeto.persistence.ColorRepository;
import eus.birt.dam.aurkitu.objeto.persistence.EstadoObjetoRepository;
import eus.birt.dam.aurkitu.objeto.persistence.ObjetoRepository;
import eus.birt.dam.aurkitu.objeto.persistence.TipoObjetoRepository;
import eus.birt.dam.aurkitu.objeto.service.impl.ObjetoServiceImpl;
import eus.birt.dam.aurkitu.payload.response.MensajeInfoResponse;
import eus.birt.dam.aurkitu.security.persistence.UsuarioRepository;
import eus.birt.dam.aurkitu.utils.Constantes;

@ExtendWith(MockitoExtension.class)
class GuardarObjetoTest {

	@Mock
	private UsuarioRepository usuarioRepo;

	@Mock
	private TipoObjetoRepository tipoObjetoRepo;

	@Mock
	private ColorRepository colorRepo;

	@Mock
	private EstadoObjetoRepository estadoObjetoRepo;

	@Mock
	private ObjetoRepository objetoRepo;

	@InjectMocks
	private ObjetoServiceImpl objetoService;

	private ObjetoDTO objetoDTO;
	private UsuarioEntity usuarioEntity;
	private TipoObjetoEntity tipoObjetoEntity;
	private ColorEntity colorEntity;
	private EstadoObjetoEntity estadoObjetoEntity;
	private ObjetoEntity objetoEntity;

	@BeforeEach
	void setup() {

		// Configurar DTO
		objetoDTO = new ObjetoDTO();
		objetoDTO.setIdTipoObjeto(1);
		objetoDTO.setIdColor(2);
		objetoDTO.setDescripcion("Móvil Samsung negro");
		objetoDTO.setFechaPerdida(Instant.now());

		// Configurar entidades
		usuarioEntity = UsuarioEntity.builder().id(1).username("testuser").build();

		tipoObjetoEntity = TipoObjetoEntity.builder().id(1).codigo("MOVIL").build();

		colorEntity = ColorEntity.builder().id(2).codigo("NEGRO").build();

		estadoObjetoEntity = EstadoObjetoEntity.builder().id(EstadoObjetoEnum.PERDIDO.ordinal()).codigo("PERDIDO")
				.build();

		objetoEntity = ObjetoEntity.builder().id(1).descripcion("Móvil Samsung negro").usuario(usuarioEntity)
				.tipo(tipoObjetoEntity).color(colorEntity).estado(estadoObjetoEntity).radio(500)
				.fechaPerdida(Instant.now()).build();
	}

	@Test
	void testGuardarObjeto_Success() {

		// Arrange
		Integer idUsuario = 1;
		SesionDTO sesion = SesionDTO.builder().id(idUsuario).build();

		Mockito.when(usuarioRepo.findById(idUsuario)).thenReturn(Optional.of(usuarioEntity));
		Mockito.when(tipoObjetoRepo.findById(objetoDTO.getIdTipoObjeto())).thenReturn(Optional.of(tipoObjetoEntity));
		Mockito.when(colorRepo.findById(objetoDTO.getIdColor())).thenReturn(Optional.of(colorEntity));
		Mockito.when(estadoObjetoRepo.findById(EstadoObjetoEnum.PERDIDO.ordinal() + 1))
				.thenReturn(Optional.of(estadoObjetoEntity));
		Mockito.when(objetoRepo.save(Mockito.any(ObjetoEntity.class))).thenReturn(objetoEntity);

		// Act
		MensajeResponse resultado = objetoService.guardarObjeto(objetoDTO, sesion, null, null);

		// Assert
		assertNotNull(resultado);
		assertEquals(Constantes.OBJETO_GUARDADO, resultado.getMensaje());

		Mockito.verify(usuarioRepo).findById(idUsuario);
		Mockito.verify(tipoObjetoRepo).findById(objetoDTO.getIdTipoObjeto());
		Mockito.verify(colorRepo).findById(objetoDTO.getIdColor());
		Mockito.verify(estadoObjetoRepo).findById(EstadoObjetoEnum.PERDIDO.ordinal() + 1);
		Mockito.verify(objetoRepo).save(Mockito.any(ObjetoEntity.class));
	}

	@Test
	void testGuardarObjeto_UsuarioNoEncontrado() {

		// Arrange
		Integer idUsuario = 999;
		SesionDTO sesion = SesionDTO.builder().id(idUsuario).build();

		Mockito.when(usuarioRepo.findById(idUsuario)).thenReturn(Optional.empty());

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class,
				() -> objetoService.guardarObjeto(objetoDTO, sesion, null, null));

		assertEquals(ErrorEnum.USER_NOT_FOUND.getStatus(), exception.getStatusCode());
		Mockito.verify(usuarioRepo).findById(idUsuario);
		Mockito.verifyNoInteractions(tipoObjetoRepo, colorRepo, estadoObjetoRepo, objetoRepo);
	}

	@Test
	void testGuardarObjeto_TipoObjetoNoEncontrado() {

		// Arrange
		Integer idUsuario = 1;
		SesionDTO sesion = SesionDTO.builder().id(idUsuario).build();

		Mockito.when(usuarioRepo.findById(idUsuario)).thenReturn(Optional.of(usuarioEntity));
		Mockito.when(tipoObjetoRepo.findById(objetoDTO.getIdTipoObjeto())).thenReturn(Optional.empty());

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class,
				() -> objetoService.guardarObjeto(objetoDTO, sesion, null, null));

		assertEquals(ErrorEnum.TIPO_OBJETO_NO_ENCONTRADO.getStatus(), exception.getStatusCode());
		Mockito.verify(usuarioRepo).findById(idUsuario);
		Mockito.verify(tipoObjetoRepo).findById(objetoDTO.getIdTipoObjeto());
		Mockito.verifyNoInteractions(colorRepo, estadoObjetoRepo, objetoRepo);
	}

	@Test
	void testGuardarObjeto_ColorNoEncontrado() {

		// Arrange
		Integer idUsuario = 1;
		SesionDTO sesion = SesionDTO.builder().id(idUsuario).build();

		Mockito.when(usuarioRepo.findById(idUsuario)).thenReturn(Optional.of(usuarioEntity));
		Mockito.when(tipoObjetoRepo.findById(objetoDTO.getIdTipoObjeto())).thenReturn(Optional.of(tipoObjetoEntity));
		Mockito.when(colorRepo.findById(objetoDTO.getIdColor())).thenReturn(Optional.empty());

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class,
				() -> objetoService.guardarObjeto(objetoDTO, sesion, null, null));

		assertEquals(ErrorEnum.COLOR_NO_ENCONTRADO.getStatus(), exception.getStatusCode());
		Mockito.verify(usuarioRepo).findById(idUsuario);
		Mockito.verify(tipoObjetoRepo).findById(objetoDTO.getIdTipoObjeto());
		Mockito.verify(colorRepo).findById(objetoDTO.getIdColor());
		Mockito.verifyNoInteractions(estadoObjetoRepo, objetoRepo);
	}

	@Test
	void testGuardarObjeto_EstadoNoEncontrado() {

		// Arrange
		Integer idUsuario = 1;
		SesionDTO sesion = SesionDTO.builder().id(idUsuario).build();

		Mockito.when(usuarioRepo.findById(idUsuario)).thenReturn(Optional.of(usuarioEntity));
		Mockito.when(tipoObjetoRepo.findById(objetoDTO.getIdTipoObjeto())).thenReturn(Optional.of(tipoObjetoEntity));
		Mockito.when(colorRepo.findById(objetoDTO.getIdColor())).thenReturn(Optional.of(colorEntity));
		Mockito.when(estadoObjetoRepo.findById(EstadoObjetoEnum.PERDIDO.ordinal() + 1)).thenReturn(Optional.empty());

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class,
				() -> objetoService.guardarObjeto(objetoDTO, sesion, null, null));

		assertEquals(ErrorEnum.ESTADO_NO_ENCONTRADO.getStatus(), exception.getStatusCode());
		Mockito.verify(usuarioRepo).findById(idUsuario);
		Mockito.verify(tipoObjetoRepo).findById(objetoDTO.getIdTipoObjeto());
		Mockito.verify(colorRepo).findById(objetoDTO.getIdColor());
		Mockito.verify(estadoObjetoRepo).findById(EstadoObjetoEnum.PERDIDO.ordinal() + 1);
		Mockito.verifyNoInteractions(objetoRepo);
	}
}