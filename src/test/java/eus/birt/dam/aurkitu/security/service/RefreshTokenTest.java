package eus.birt.dam.aurkitu.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import eus.birt.dam.aurkitu.model.RefreshTokenEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;
import eus.birt.dam.aurkitu.payload.response.LoginResponse;
import eus.birt.dam.aurkitu.security.jwt.JwtUtils;
import eus.birt.dam.aurkitu.security.persistence.RefreshTokenRepository;
import eus.birt.dam.aurkitu.security.persistence.UsuarioRepository;
import eus.birt.dam.aurkitu.security.service.impl.AuthServiceImpl;

@ExtendWith(MockitoExtension.class)
class RefreshTokenTest {

	@Mock
	private UsuarioRepository usuarioRepo;

	@Mock
	private RefreshTokenRepository refreshTokenRepo;

	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private AuthServiceImpl authService;

	private UsuarioEntity usuarioEntity;
	private RefreshTokenEntity refreshTokenEntity;
	private final String validAccessToken = "valid.access.token";
	private final String authHeader = "Bearer " + validAccessToken;

	@BeforeEach
	void setup() {
		usuarioEntity = UsuarioEntity.builder().id(1).username("birt").email("birt@birt.eus").password("password123")
				.verificado(true).build();

		refreshTokenEntity = RefreshTokenEntity.builder().id(1).usuario(usuarioEntity).token("old-refresh-token")
				.expiracion(Instant.now().plus(1, ChronoUnit.HOURS)).build();
	}

	@Test
	void testRefreshToken_Success() {

		// Arrange
		Mockito.when(jwtUtils.getUserIdFromToken(validAccessToken)).thenReturn(1);
		Mockito.when(refreshTokenRepo.findByUsuarioId(1)).thenReturn(Optional.of(refreshTokenEntity));
		Mockito.when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuarioEntity));
		Mockito.when(jwtUtils.generateAccessToken(usuarioEntity)).thenReturn("new-access-token");
		Mockito.when(refreshTokenRepo.save(Mockito.any(RefreshTokenEntity.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		LoginResponse resultado = authService.refreshToken(authHeader);

		// Assert
		assertNotNull(resultado);
		assertEquals("new-access-token", resultado.getAccessToken());

		Mockito.verify(jwtUtils).getUserIdFromToken(validAccessToken);
		Mockito.verify(refreshTokenRepo).findByUsuarioId(1);
		Mockito.verify(refreshTokenRepo).delete(refreshTokenEntity);
		Mockito.verify(usuarioRepo).findById(1);
		Mockito.verify(jwtUtils).generateAccessToken(usuarioEntity);
		Mockito.verify(refreshTokenRepo).save(Mockito.any(RefreshTokenEntity.class));
	}

	@Test
	void testRefreshToken_HeaderNull() {

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class, () -> authService.refreshToken(null));

		assertEquals(ErrorEnum.SESION_ERROR.getStatus(), exception.getStatusCode());
	}

	@Test
	void testRefreshToken_HeaderSinBearer() {

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class,
				() -> authService.refreshToken("Basic token"));

		assertEquals(ErrorEnum.SESION_ERROR.getStatus(), exception.getStatusCode());
	}

	@Test
	void testRefreshToken_RefreshTokenNoEncontrado() {

		// Arrange
		Mockito.when(jwtUtils.getUserIdFromToken(validAccessToken)).thenReturn(1);
		Mockito.when(refreshTokenRepo.findByUsuarioId(1)).thenReturn(Optional.empty());

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class, () -> authService.refreshToken(authHeader));

		assertEquals(ErrorEnum.REFRESH_TOKEN_INVALIDO.getStatus(), exception.getStatusCode());
		Mockito.verify(jwtUtils).getUserIdFromToken(validAccessToken);
		Mockito.verify(refreshTokenRepo).findByUsuarioId(1);
		Mockito.verify(refreshTokenRepo, Mockito.never()).delete(Mockito.any());
	}

	@Test
	void testRefreshToken_RefreshTokenExpirado() {
		// Arrange

		refreshTokenEntity.setExpiracion(Instant.now().minus(1, ChronoUnit.HOURS));
		Mockito.when(jwtUtils.getUserIdFromToken(validAccessToken)).thenReturn(1);
		Mockito.when(refreshTokenRepo.findByUsuarioId(1)).thenReturn(Optional.of(refreshTokenEntity));

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class, () -> authService.refreshToken(authHeader));

		assertEquals(ErrorEnum.REFRESH_TOKEN_CADUCADO.getStatus(), exception.getStatusCode());
		Mockito.verify(jwtUtils).getUserIdFromToken(validAccessToken);
		Mockito.verify(refreshTokenRepo).findByUsuarioId(1);
		Mockito.verify(refreshTokenRepo).delete(refreshTokenEntity);
		Mockito.verify(usuarioRepo, Mockito.never()).findById(Mockito.anyInt());
	}

	@Test
	void testRefreshToken_UsuarioNoEncontrado() {

		// Arrange
		Mockito.when(jwtUtils.getUserIdFromToken(validAccessToken)).thenReturn(1);
		Mockito.when(refreshTokenRepo.findByUsuarioId(1)).thenReturn(Optional.of(refreshTokenEntity));
		Mockito.when(usuarioRepo.findById(1)).thenReturn(Optional.empty());

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class, () -> authService.refreshToken(authHeader));

		assertEquals(ErrorEnum.USER_NOT_FOUND.getStatus(), exception.getStatusCode());
		Mockito.verify(jwtUtils).getUserIdFromToken(validAccessToken);
		Mockito.verify(refreshTokenRepo).findByUsuarioId(1);
		Mockito.verify(refreshTokenRepo).delete(refreshTokenEntity);
		Mockito.verify(usuarioRepo).findById(1);
		Mockito.verify(refreshTokenRepo, Mockito.never()).save(Mockito.any());
	}
}