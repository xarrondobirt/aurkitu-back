package eus.birt.dam.aurkitu.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.DigestUtils;

import eus.birt.dam.aurkitu.dto.UsuarioDTO;
import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import eus.birt.dam.aurkitu.mail.service.MailService;
import eus.birt.dam.aurkitu.model.UsuarioEntity;
import eus.birt.dam.aurkitu.payload.response.RegistroUsuarioResponse;
import eus.birt.dam.aurkitu.security.persistence.CodigoVerificacionRepository;
import eus.birt.dam.aurkitu.security.persistence.UsuarioRepository;
import eus.birt.dam.aurkitu.security.service.impl.UsuarioServiceImpl;
import eus.birt.dam.aurkitu.utils.Constantes;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

	@Mock
	private UsuarioRepository userRepo;

	@Mock
	private CodigoVerificacionRepository codVerificacionRepo;

	@Mock
	private MailService mailService;

	@InjectMocks
	private UsuarioServiceImpl usuarioService;

	private UsuarioDTO usuarioDTO;
	private UsuarioEntity usuarioEntity;

	@BeforeEach
	void setup() {
		usuarioDTO = new UsuarioDTO();
		usuarioDTO.setUsername("birt");
		usuarioDTO.setEmail("birt@birt.eus");
		usuarioDTO.setPassword("password123");

		usuarioEntity = UsuarioEntity.builder().username("birt").email("tbirt@birt.eus")
				.password(DigestUtils.md5DigestAsHex("password123".getBytes())).build();
	}

	@Test
	void testRegistrarUsuario_Success() {

		// Arrange
		Mockito.when(userRepo.existsByEmail("birt@birt.eus")).thenReturn(false);
		Mockito.when(userRepo.existsByUsername("birt")).thenReturn(false);
		Mockito.when(userRepo.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

		// Act
		RegistroUsuarioResponse resultado = usuarioService.registrarUsuario(usuarioDTO);

		// Assert
		assertNotNull(resultado);
		assertEquals(Constantes.USUARIO_SIN_VERIFICAR, resultado.getMensaje());
		Mockito.verify(userRepo).existsByEmail("birt@birt.eus");
		Mockito.verify(userRepo).existsByUsername("birt");
		Mockito.verify(userRepo).save(any(UsuarioEntity.class));
	}

	@Test
	void testRegistrarUsuario_EmailAlreadyExists() {

		// Arrange
		Mockito.when(userRepo.existsByEmail("birt@birt.eus")).thenReturn(true);

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class, () -> {
			usuarioService.registrarUsuario(usuarioDTO);
		});

		assertEquals(ErrorEnum.EMAIL_ALREADY_EXISTS.getStatus(), exception.getStatusCode());
		Mockito.verify(userRepo, Mockito.never()).save(any());
	}

	@Test
	void testRegistrarUsuario_UsernameAlreadyExists() {

		// Arrange
		Mockito.when(userRepo.existsByEmail("birt@birt.eus")).thenReturn(false);
		Mockito.when(userRepo.existsByUsername("birt")).thenReturn(true);

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class,
				() -> usuarioService.registrarUsuario(usuarioDTO));

		assertEquals(ErrorEnum.USERNAME_ALREADY_EXISTS.getStatus(), exception.getStatusCode());
		Mockito.verify(userRepo, Mockito.never()).save(any());
	}
}