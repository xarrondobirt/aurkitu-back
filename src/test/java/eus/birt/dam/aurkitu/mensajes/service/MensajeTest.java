package eus.birt.dam.aurkitu.mensajes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.enums.EstadoObjetoEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import eus.birt.dam.aurkitu.mensajes.persistence.ConversacionRepository;
import eus.birt.dam.aurkitu.mensajes.persistence.MensajeRepository;
import eus.birt.dam.aurkitu.mensajes.service.impl.MensajeServiceImpl;
import eus.birt.dam.aurkitu.model.ConversacionEntity;
import eus.birt.dam.aurkitu.model.EstadoObjetoEntity;
import eus.birt.dam.aurkitu.model.MensajeEntity;
import eus.birt.dam.aurkitu.model.ObjetoEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;
import eus.birt.dam.aurkitu.objeto.persistence.EstadoObjetoRepository;
import eus.birt.dam.aurkitu.objeto.persistence.ObjetoRepository;
import eus.birt.dam.aurkitu.payload.request.EnviarMensajeRequest;
import eus.birt.dam.aurkitu.payload.response.ConversacionResponse;
import eus.birt.dam.aurkitu.payload.response.MensajeInfoResponse;
import eus.birt.dam.aurkitu.security.persistence.UsuarioRepository;
import eus.birt.dam.aurkitu.utils.Constantes;

@ExtendWith(MockitoExtension.class)
class MensajeTest {

	@Mock
	private ConversacionRepository conversacionRepo;

	@Mock
	private MensajeRepository mensajeRepo;

	@Mock
	private UsuarioRepository usuarioRepo;

	@Mock
	private ObjetoRepository objetoRepo;

	@Mock
	private EstadoObjetoRepository estadoObjetoRepo;

	@InjectMocks
	private MensajeServiceImpl mensajeService;

	private SesionDTO sesion;
	private UsuarioEntity usuarioRemitente;
	private UsuarioEntity usuarioDestinatario;
	private ObjetoEntity objeto;
	private ConversacionEntity conversacion;
	private MensajeEntity mensaje;

	@BeforeEach
	void setup() {

		sesion = SesionDTO.builder().id(1).username("usuario1").build();
		usuarioRemitente = UsuarioEntity.builder().id(1).username("usuario1").build();
		usuarioDestinatario = UsuarioEntity.builder().id(2).username("usuario2").build();
		objeto = ObjetoEntity.builder().id(100).descripcion("Móvil perdido").usuario(usuarioDestinatario).build();
		conversacion = ConversacionEntity.builder().id(10).participante1(usuarioRemitente)
				.participante2(usuarioDestinatario).objeto(objeto).lastUpdateDate(Instant.now()).build();
		mensaje = MensajeEntity.builder().id(20).conversacion(conversacion).remitente(usuarioRemitente)
				.contenido("Hola, ¿has visto mi móvil?").leido(false).createDate(Instant.now()).build();

		conversacion.setMensajes(List.of(mensaje));
	}

	@Test
	void testEnviarMensaje_Success() {

		// Arrange
		EnviarMensajeRequest request = new EnviarMensajeRequest();
		request.setIdDestinatario(2);
		request.setIdObjeto(100);
		request.setContenido("Hola, ¿has visto mi móvil?");

		Mockito.when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuarioRemitente));
		Mockito.when(usuarioRepo.findById(2)).thenReturn(Optional.of(usuarioDestinatario));
		Mockito.when(objetoRepo.findById(100)).thenReturn(Optional.of(objeto));
		Mockito.when(conversacionRepo.save(Mockito.any(ConversacionEntity.class))).thenReturn(conversacion);
		Mockito.when(mensajeRepo.save(Mockito.any(MensajeEntity.class))).thenReturn(mensaje);

		// Act
		MensajeInfoResponse resultado = mensajeService.enviarMensaje(sesion, request);

		// Assert
		assertNotNull(resultado);
		assertEquals(Constantes.MENSAJE_ENVIADO, resultado.getMensaje());

		Mockito.verify(usuarioRepo, Mockito.times(2)).findById(Mockito.anyInt());
		Mockito.verify(objetoRepo).findById(100);
		Mockito.verify(conversacionRepo, Mockito.times(2)).save(Mockito.any(ConversacionEntity.class));
		Mockito.verify(mensajeRepo).save(Mockito.any(MensajeEntity.class));
	}

	@Test
	void testEnviarMensaje_UsuarioNoEncontrado() {

		// Arrange
		EnviarMensajeRequest request = new EnviarMensajeRequest();
		request.setIdDestinatario(999); // ID inexistente

		Mockito.when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuarioRemitente));
		Mockito.when(usuarioRepo.findById(999)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(AurkituException.class, () -> mensajeService.enviarMensaje(sesion, request));
	}

	@Test
	void testObtenerConversacionesUsuario_Success() {

		// Arrange
		ConversacionEntity conv1 = ConversacionEntity.builder().id(1).participante1(usuarioRemitente)
				.participante2(usuarioDestinatario).lastUpdateDate(Instant.now().minusSeconds(3600)).objeto(objeto)
				.build();
		ConversacionEntity conv2 = ConversacionEntity.builder().id(2).participante1(usuarioDestinatario)
				.participante2(UsuarioEntity.builder().id(3).username("usuario3").build()).objeto(objeto)
				.lastUpdateDate(Instant.now()).build();

		Mockito.when(conversacionRepo.findByParticipante1IdOrParticipante2Id(1, 1)).thenReturn(Set.of(conv1, conv2));

		// Act
		List<ConversacionResponse> resultado = mensajeService.obtenerConversacionesUsuario(sesion);

		// Assert
		assertNotNull(resultado);
		assertEquals(2, resultado.size());
		assertEquals(2, resultado.get(0).getId());
		assertEquals(1, resultado.get(1).getId());
	}

	@Test
	void testObtenerMensajes_ConversacionNoEncontrada() {

		// Arrange
		Integer idConversacionInexistente = 999;
		Mockito.when(conversacionRepo.findById(idConversacionInexistente)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(AurkituException.class, () -> mensajeService.obtenerMensajes(idConversacionInexistente, sesion));
	}

	@Test
	void testCerrarCaso_CambiaEstadoADevuelto() {

		// Arrange
		Integer idObjeto = 100;
		EstadoObjetoEntity estadoDevuelto = EstadoObjetoEntity.builder().id(EstadoObjetoEnum.DEVUELTO.ordinal() + 1)
				.codigo("DEVUELTO").build();

		Mockito.when(objetoRepo.findById(idObjeto)).thenReturn(Optional.of(objeto));

		Mockito.when(estadoObjetoRepo.findById(EstadoObjetoEnum.DEVUELTO.ordinal() + 1))
				.thenReturn(Optional.of(estadoDevuelto));

		// Act
		MensajeInfoResponse resultado = mensajeService.cerrarCaso(sesion, idObjeto);

		// Assert
		assertNotNull(resultado);
		assertEquals(Constantes.CASO_CERRADO, resultado.getMensaje());
		assertEquals(estadoDevuelto, objeto.getEstado());

		Mockito.verify(objetoRepo, times(1)).findById(idObjeto);
		Mockito.verify(estadoObjetoRepo, times(1)).findById(EstadoObjetoEnum.DEVUELTO.ordinal() + 1);
		Mockito.verify(objetoRepo, times(1)).save(objeto);
	}
}