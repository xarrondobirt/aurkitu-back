package eus.birt.proyecto.security.service;

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

import eus.birt.proyecto.dto.UsuarioDTO;
import eus.birt.proyecto.enums.ErrorEnum;
import eus.birt.proyecto.exception.CustomResponseStatusException;
import eus.birt.proyecto.mail.service.MailService;
import eus.birt.proyecto.model.UsuarioEntity;
import eus.birt.proyecto.payload.response.RegistroUsuarioResponse;
import eus.birt.proyecto.security.persistence.CodigoVerificacionRepository;
import eus.birt.proyecto.security.persistence.UserRepository;
import eus.birt.proyecto.security.service.impl.AuthServiceImpl;
import eus.birt.proyecto.utils.Constantes;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UserRepository userRepo;

	@Mock
	private CodigoVerificacionRepository codVerificacionRepo;

	@Mock
	private MailService mailService;

	@InjectMocks
	private AuthServiceImpl userService;

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
		RegistroUsuarioResponse resultado = userService.registrarUsuario(usuarioDTO);

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
		CustomResponseStatusException exception = assertThrows(CustomResponseStatusException.class, () -> {
			userService.registrarUsuario(usuarioDTO);
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
		CustomResponseStatusException exception = assertThrows(CustomResponseStatusException.class,
				() -> userService.registrarUsuario(usuarioDTO));

		assertEquals(ErrorEnum.USERNAME_ALREADY_EXISTS.getStatus(), exception.getStatusCode());
		Mockito.verify(userRepo, Mockito.never()).save(any());
	}
}