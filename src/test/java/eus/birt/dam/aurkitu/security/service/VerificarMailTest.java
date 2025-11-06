package eus.birt.dam.aurkitu.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import eus.birt.dam.aurkitu.model.CodigoVerificacionEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;
import eus.birt.dam.aurkitu.payload.request.RegistroUsuarioRequest;
import eus.birt.dam.aurkitu.payload.response.MensajeResponse;
import eus.birt.dam.aurkitu.security.persistence.CodigoVerificacionRepository;
import eus.birt.dam.aurkitu.security.persistence.UsuarioRepository;
import eus.birt.dam.aurkitu.security.service.impl.AuthServiceImpl;
import eus.birt.dam.aurkitu.utils.Constantes;

@ExtendWith(MockitoExtension.class)
class VerificarMailTest {

	@Mock
	private UsuarioRepository usuarioRepo;

	@Mock
	private CodigoVerificacionRepository codVerificacionRepo;

	@InjectMocks
	private AuthServiceImpl authService;

	private UsuarioEntity usuarioEntity;
	private CodigoVerificacionEntity codigoVerificacionEntity;
	private RegistroUsuarioRequest registroRequest;

	@BeforeEach
	void setup() {

		usuarioEntity = UsuarioEntity.builder().id(1).username("birt").email("birt@birt.eus").password("password123")
				.verificado(false).build();

		codigoVerificacionEntity = CodigoVerificacionEntity.builder().id(1).usuario(usuarioEntity).codigo("123456")
				.build();

		registroRequest = new RegistroUsuarioRequest();
		registroRequest.setIdUsuario(1);
		registroRequest.setCodigoVerificacion("123456");
	}

	@Test
	void testVerificarCodigo_Success() {

		// Arrange
		Mockito.when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuarioEntity));
		Mockito.when(codVerificacionRepo.findByUsuarioIdAndCodigo(1, "123456"))
				.thenReturn(Optional.of(codigoVerificacionEntity));
		Mockito.when(usuarioRepo.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

		// Act
		MensajeResponse resultado = authService.verificarCodigo(registroRequest);

		// Assert
		assertNotNull(resultado);
		assertEquals(Constantes.EMAIL_VERIFICADO, resultado.getMensaje());
		assertTrue(usuarioEntity.isVerificado());

		Mockito.verify(usuarioRepo).findById(1);
		Mockito.verify(codVerificacionRepo).findByUsuarioIdAndCodigo(1, "123456");
		Mockito.verify(usuarioRepo).save(usuarioEntity);
	}

	@Test
	void testVerificarCodigo_UsuarioNotFound() {

		// Arrange
		Mockito.when(usuarioRepo.findById(1)).thenReturn(Optional.empty());

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class,
				() -> authService.verificarCodigo(registroRequest));

		assertEquals(ErrorEnum.USER_NOT_FOUND.getStatus(), exception.getStatusCode());

		Mockito.verify(usuarioRepo).findById(1);
		Mockito.verify(codVerificacionRepo, Mockito.never()).findByUsuarioIdAndCodigo(Mockito.anyInt(),
				Mockito.anyString());
		Mockito.verify(usuarioRepo, Mockito.never()).save(any(UsuarioEntity.class));
	}

	@Test
	void testVerificarCodigo_UsuarioYaVerificado() {

		// Arrange
		usuarioEntity.setVerificado(true);
		Mockito.when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuarioEntity));

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class,
				() -> authService.verificarCodigo(registroRequest));

		assertEquals(ErrorEnum.USERNAME_ALREADY_VERIFIED.getStatus(), exception.getStatusCode());
		Mockito.verify(usuarioRepo).findById(1);
		Mockito.verify(codVerificacionRepo, Mockito.never()).findByUsuarioIdAndCodigo(Mockito.anyInt(), anyString());
		Mockito.verify(usuarioRepo, Mockito.never()).save(any(UsuarioEntity.class));
	}

	@Test
	void testVerificarCodigo_CodigoNotFound() {

		// Arrange
		Mockito.when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuarioEntity));
		Mockito.when(codVerificacionRepo.findByUsuarioIdAndCodigo(1, "123456")).thenReturn(Optional.empty());

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class,
				() -> authService.verificarCodigo(registroRequest));

		assertEquals(ErrorEnum.VERIFICATION_CODE_NOT_FOUND.getStatus(), exception.getStatusCode());
		Mockito.verify(usuarioRepo).findById(1);
		Mockito.verify(codVerificacionRepo).findByUsuarioIdAndCodigo(1, "123456");
		Mockito.verify(usuarioRepo, Mockito.never()).save(any(UsuarioEntity.class));
	}
}