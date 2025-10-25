package eus.birt.dam.aurkitu.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.enums.HtmlTemplateEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import eus.birt.dam.aurkitu.mail.service.MailService;
import eus.birt.dam.aurkitu.model.CodigoVerificacionEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;
import eus.birt.dam.aurkitu.payload.response.MensajeResponse;
import eus.birt.dam.aurkitu.security.persistence.CodigoVerificacionRepository;
import eus.birt.dam.aurkitu.security.persistence.UsuarioRepository;
import eus.birt.dam.aurkitu.security.service.impl.AuthServiceImpl;
import eus.birt.dam.aurkitu.utils.Constantes;

@ExtendWith(MockitoExtension.class)
class RecuperarPasswordTest {

	@Mock
	private UsuarioRepository usuarioRepo;

	@Mock
	private CodigoVerificacionRepository codVerificacionRepo;

	@Mock
	private MailService mailService;

	@InjectMocks
	private AuthServiceImpl authService;

	private UsuarioEntity usuarioEntity;
	private final String email = "test@aurkitu.eus";

	@BeforeEach
	void setup() {
		usuarioEntity = UsuarioEntity.builder().id(1).username("testuser").email(email).password("password123")
				.verificado(true).build();
	}

	@Test
	void testRecuperarPassword_Success() {

		// Arrange
		Mockito.when(usuarioRepo.findByEmail(email)).thenReturn(Optional.of(usuarioEntity));
		Mockito.when(codVerificacionRepo.save(Mockito.any(CodigoVerificacionEntity.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		MensajeResponse resultado = authService.recuperarPassword(email);

		// Assert
		assertNotNull(resultado);
		assertEquals(Constantes.EMAIL_RECUPERAR_PASSWORD, resultado.getMensaje());

		Mockito.verify(usuarioRepo).findByEmail(email);
		Mockito.verify(codVerificacionRepo).save(Mockito.any(CodigoVerificacionEntity.class));
		Mockito.verify(mailService).enviarCodigo(Mockito.eq(email), Mockito.anyString(),
				Mockito.eq(HtmlTemplateEnum.RECUPERAR_PASSWORD.toString()),
				Mockito.eq(Constantes.ASUNTO_RESET_PASSWORD));
	}

	@Test
	void testRecuperarPassword_UsuarioNoEncontrado() {

		// Arrange
		Mockito.when(usuarioRepo.findByEmail(email)).thenReturn(Optional.empty());

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class, () -> authService.recuperarPassword(email));

		assertEquals(ErrorEnum.USER_NOT_FOUND.getStatus(), exception.getStatusCode());
		Mockito.verify(usuarioRepo).findByEmail(email);
		Mockito.verify(codVerificacionRepo, Mockito.never()).save(Mockito.any());
		Mockito.verify(mailService, Mockito.never()).enviarCodigo(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void testRecuperarPassword_UsuarioNoVerificado() {

		// Arrange
		usuarioEntity.setVerificado(false);
		Mockito.when(usuarioRepo.findByEmail(email)).thenReturn(Optional.of(usuarioEntity));

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class, () -> authService.recuperarPassword(email));

		assertEquals(ErrorEnum.BAD_CREDENTIALS.getStatus(), exception.getStatusCode());
		Mockito.verify(usuarioRepo).findByEmail(email);
		Mockito.verify(codVerificacionRepo, Mockito.never()).save(Mockito.any());
		Mockito.verify(mailService, Mockito.never()).enviarCodigo(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void testRecuperarPassword_GuardaCodigoConUsuarioCorrecto() {

		// Arrange
		Mockito.when(usuarioRepo.findByEmail(email)).thenReturn(Optional.of(usuarioEntity));
		Mockito.when(codVerificacionRepo.save(Mockito.any(CodigoVerificacionEntity.class))).thenAnswer(invocation -> {
			CodigoVerificacionEntity codigoEntity = invocation.getArgument(0);

			// Verificar que el código se guarda con el usuario correcto
			assertEquals(usuarioEntity, codigoEntity.getUsuario());
			assertNotNull(codigoEntity.getCodigo());
			return codigoEntity;
		});

		// Act
		MensajeResponse resultado = authService.recuperarPassword(email);

		// Assert
		assertNotNull(resultado);
		Mockito.verify(usuarioRepo).findByEmail(email);
		Mockito.verify(codVerificacionRepo).save(Mockito.any(CodigoVerificacionEntity.class));
	}
}