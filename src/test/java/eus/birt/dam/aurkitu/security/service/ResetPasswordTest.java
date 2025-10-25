package eus.birt.dam.aurkitu.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import eus.birt.dam.aurkitu.model.CodigoVerificacionEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;
import eus.birt.dam.aurkitu.payload.request.ResetPasswordRequest;
import eus.birt.dam.aurkitu.security.persistence.CodigoVerificacionRepository;
import eus.birt.dam.aurkitu.security.persistence.UsuarioRepository;
import eus.birt.dam.aurkitu.security.service.impl.AuthServiceImpl;

@ExtendWith(MockitoExtension.class)
class ResetPasswordTest {

	@Mock
	private UsuarioRepository usuarioRepo;

	@Mock
	private CodigoVerificacionRepository codVerificacionRepo;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private AuthServiceImpl authService;

	private UsuarioEntity usuarioEntity;
	private CodigoVerificacionEntity codigoVerificacionEntity;
	private ResetPasswordRequest resetRequest;

	@BeforeEach
	void setup() {
		usuarioEntity = UsuarioEntity.builder().id(1).username("testuser").email("test@aurkitu.eus")
				.password("oldPassword").verificado(true).build();

		codigoVerificacionEntity = CodigoVerificacionEntity.builder().id(1).usuario(usuarioEntity).codigo("123456")
				.build();

		resetRequest = new ResetPasswordRequest();
		resetRequest.setIdUsuario(1);
		resetRequest.setNuevaPassword("newPassword123");
		resetRequest.setRepitePassword("newPassword123");
		resetRequest.setCodVerificacion("123456");
	}

	@Test
	void testResetPassword_Success() {

		// Arrange
		Mockito.when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuarioEntity));
		Mockito.when(codVerificacionRepo.findByUsuarioIdAndCodigo(1, "123456"))
				.thenReturn(Optional.of(codigoVerificacionEntity));
		Mockito.when(passwordEncoder.encode("newPassword123")).thenReturn("encodedNewPassword");
		Mockito.when(usuarioRepo.save(Mockito.any(UsuarioEntity.class))).thenReturn(usuarioEntity);

		// Act
		authService.resetPassword(resetRequest);

		// Assert
		Mockito.verify(usuarioRepo).findById(1);
		Mockito.verify(codVerificacionRepo).findByUsuarioIdAndCodigo(1, "123456");
		Mockito.verify(passwordEncoder).encode("newPassword123");
		Mockito.verify(usuarioRepo).save(usuarioEntity);
		Mockito.verify(codVerificacionRepo).delete(codigoVerificacionEntity);
		assertEquals("encodedNewPassword", usuarioEntity.getPassword());
	}

	@Test
	void testResetPassword_PasswordsNoCoinciden() {

		// Arrange
		resetRequest.setRepitePassword("differentPassword");

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class,
				() -> authService.resetPassword(resetRequest));

		assertEquals(ErrorEnum.PASSWORD_NO_COINCIDEN.getStatus(), exception.getStatusCode());
		Mockito.verify(usuarioRepo, Mockito.never()).findById(Mockito.anyInt());
	}

	@Test
	void testResetPassword_UsuarioNoEncontrado() {

		// Arrange
		Mockito.when(usuarioRepo.findById(1)).thenReturn(Optional.empty());

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class,
				() -> authService.resetPassword(resetRequest));

		assertEquals(ErrorEnum.USER_NOT_FOUND.getStatus(), exception.getStatusCode());
		Mockito.verify(usuarioRepo).findById(1);
		Mockito.verify(codVerificacionRepo, Mockito.never()).findByUsuarioIdAndCodigo(Mockito.anyInt(),
				Mockito.anyString());
	}

	@Test
	void testResetPassword_CodigoNoEncontrado() {

		// Arrange
		Mockito.when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuarioEntity));
		Mockito.when(codVerificacionRepo.findByUsuarioIdAndCodigo(1, "123456")).thenReturn(Optional.empty());

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class,
				() -> authService.resetPassword(resetRequest));

		assertEquals(ErrorEnum.VERIFICATION_CODE_NOT_FOUND.getStatus(), exception.getStatusCode());
		Mockito.verify(usuarioRepo).findById(1);
		Mockito.verify(codVerificacionRepo).findByUsuarioIdAndCodigo(1, "123456");
	}
}