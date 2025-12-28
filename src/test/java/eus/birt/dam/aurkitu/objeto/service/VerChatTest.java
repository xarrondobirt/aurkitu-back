package eus.birt.dam.aurkitu.objeto.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.exception.AurkituException;
import eus.birt.dam.aurkitu.mensajes.persistence.ConversacionRepository;
import eus.birt.dam.aurkitu.mensajes.persistence.MensajeRepository;
import eus.birt.dam.aurkitu.mensajes.service.MensajeService;
import eus.birt.dam.aurkitu.model.ConversacionEntity;
import eus.birt.dam.aurkitu.model.MensajeEntity;
import eus.birt.dam.aurkitu.model.ObjetoEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;
import eus.birt.dam.aurkitu.objeto.persistence.ObjetoRepository;
import eus.birt.dam.aurkitu.objeto.service.impl.ObjetoServiceImpl;
import eus.birt.dam.aurkitu.payload.response.ConversacionDetalleResponse;
import eus.birt.dam.aurkitu.security.persistence.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class VerChatTest {

	@Mock
	private UsuarioRepository usuarioRepo;

	@Mock
	private ObjetoRepository objetoRepo;

	@Mock
	private ConversacionRepository conversacionRepo;

	@Mock
	private MensajeRepository mensajeRepo;

	@Mock
	private MensajeService mensajeService;

	@InjectMocks
	private ObjetoServiceImpl objetoService;

	private SesionDTO sesionDTO;
	private UsuarioEntity remitente;
	private UsuarioEntity destinatario;
	private ObjetoEntity objeto;
	private ConversacionEntity conversacion;
	private List<MensajeEntity> mensajes;

	@BeforeEach
	void setup() {

		sesionDTO = SesionDTO.builder().id(1).build();
		remitente = UsuarioEntity.builder().id(1).username("Juan").build();
		destinatario = UsuarioEntity.builder().id(2).username("María").build();
		objeto = ObjetoEntity.builder().id(100).descripcion("Libro de Java").build();
		mensajes = Arrays.asList(
				MensajeEntity.builder().id(1).remitente(remitente).leido(false).contenido("Hola María").build(),
				MensajeEntity.builder().id(2).remitente(destinatario).leido(false).contenido("Hola Juan, ¿cómo estás?")
						.build(),
				MensajeEntity.builder().id(3).remitente(remitente).leido(true).contenido("Bien, gracias").build());

		conversacion = ConversacionEntity.builder().id(10).participante1(remitente).participante2(destinatario)
				.objeto(objeto).mensajes(mensajes).build();
	}

	@Test
	void testVerChat_ConversacionExistente_MensajesNoLeidos() {

		// Arrange
		Integer idUsuario = 2;
		Integer idObjeto = 100;

		Mockito.when(usuarioRepo.findById(1)).thenReturn(Optional.of(remitente));
		Mockito.when(usuarioRepo.findById(2)).thenReturn(Optional.of(destinatario));
		Mockito.when(objetoRepo.findById(100)).thenReturn(Optional.of(objeto));
		Mockito.when(mensajeService.obtenerCrearConversacion(remitente, destinatario, objeto)).thenReturn(conversacion);

		// Act
		ConversacionDetalleResponse resultado = objetoService.verChat(sesionDTO, idUsuario, idObjeto);

		// Assert
		assertNotNull(resultado);
		assertEquals(3, resultado.getMensajes().size());

	}

	@Test
	void testVerChat_UsuarioNoEncontrado() {

		// Arrange
		Integer idUsuario = 2;
		Integer idObjeto = 100;

		Mockito.when(usuarioRepo.findById(1)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(AurkituException.class, () -> objetoService.verChat(sesionDTO, idUsuario, idObjeto));

		Mockito.verify(usuarioRepo).findById(1);
		Mockito.verifyNoInteractions(objetoRepo);
		Mockito.verifyNoInteractions(mensajeService);
	}

	@Test
	void testVerChat_DestinatarioNoEncontrado() {

		// Arrange
		Integer idUsuario = 2;
		Integer idObjeto = 100;

		Mockito.when(usuarioRepo.findById(1)).thenReturn(Optional.of(remitente));
		Mockito.when(usuarioRepo.findById(2)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(AurkituException.class, () -> objetoService.verChat(sesionDTO, idUsuario, idObjeto));

		Mockito.verify(usuarioRepo).findById(1);
		Mockito.verify(usuarioRepo).findById(2);
		Mockito.verifyNoInteractions(objetoRepo);
		Mockito.verifyNoInteractions(mensajeService);
	}

	@Test
	void testVerChat_ObjetoNoEncontrado() {

		// Arrange
		Integer idUsuario = 2;
		Integer idObjeto = 100;

		Mockito.when(usuarioRepo.findById(1)).thenReturn(Optional.of(remitente));
		Mockito.when(usuarioRepo.findById(2)).thenReturn(Optional.of(destinatario));
		Mockito.when(objetoRepo.findById(100)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(AurkituException.class, () -> objetoService.verChat(sesionDTO, idUsuario, idObjeto));

		Mockito.verify(usuarioRepo).findById(1);
		Mockito.verify(usuarioRepo).findById(2);
		Mockito.verify(objetoRepo).findById(100);
		Mockito.verifyNoInteractions(mensajeService);
	}
}