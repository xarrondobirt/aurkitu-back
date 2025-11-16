package eus.birt.dam.aurkitu.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

import java.time.Instant;
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
import eus.birt.dam.aurkitu.payload.request.RefreshTokenRequest;
import eus.birt.dam.aurkitu.payload.response.LoginResponse;
import eus.birt.dam.aurkitu.security.jwt.JwtUtils;
import eus.birt.dam.aurkitu.security.persistence.RefreshTokenRepository;
import eus.birt.dam.aurkitu.security.persistence.UsuarioRepository;
import eus.birt.dam.aurkitu.security.service.impl.AuthServiceImpl;

@ExtendWith(MockitoExtension.class)
class RefreshTokenTest {

	@Mock
	private RefreshTokenRepository refreshTokenRepo;

	@Mock
	private UsuarioRepository usuarioRepo;

	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private AuthServiceImpl authService;

	private RefreshTokenRequest refreshTokenRequest;
	private RefreshTokenEntity refreshTokenEntity;
	private UsuarioEntity usuarioEntity;

	@BeforeEach
	void setup() {
		refreshTokenRequest = new RefreshTokenRequest();
		refreshTokenRequest.setToken("valid-refresh-token");

		usuarioEntity = UsuarioEntity.builder().id(1).username("testuser").email("test@test.com").build();

		refreshTokenEntity = RefreshTokenEntity.builder().id(1).token("valid-refresh-token").usuario(usuarioEntity)
				.expiracion(Instant.now().plusMillis(3600000)).build();
	}

	@Test
	void testRefreshToken_Success() {

		// Arrange
		String newAccessToken = "new-access-token";

		Mockito.when(refreshTokenRepo.findByToken("valid-refresh-token")).thenReturn(Optional.of(refreshTokenEntity));
		Mockito.when(jwtUtils.generateAccessToken(usuarioEntity)).thenReturn(newAccessToken);
		Mockito.when(refreshTokenRepo.save(Mockito.any(RefreshTokenEntity.class))).thenAnswer(invocation -> {
			RefreshTokenEntity savedToken = invocation.getArgument(0);
			savedToken.setId(2);
			return savedToken;
		});

		// Act
		LoginResponse resultado = authService.refreshToken(refreshTokenRequest);

		// Assert
		assertNotNull(resultado);
		assertEquals(newAccessToken, resultado.getAccessToken());
		assertNotNull(resultado.getRefreshToken());

		Mockito.verify(refreshTokenRepo).delete(refreshTokenEntity);
		Mockito.verify(refreshTokenRepo).save(Mockito.any(RefreshTokenEntity.class));
	}

	@Test
	void testRefreshToken_RefreshTokenInvalido() {

		// Arrange
		Mockito.when(refreshTokenRepo.findByToken("invalid-token")).thenReturn(Optional.empty());

		refreshTokenRequest.setToken("invalid-token");

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class,
				() -> authService.refreshToken(refreshTokenRequest));

		assertEquals(ErrorEnum.REFRESH_TOKEN_INVALIDO.getStatus(), exception.getStatusCode());
		Mockito.verify(refreshTokenRepo, never()).delete(any());
		Mockito.verify(refreshTokenRepo, never()).save(any());
	}

	@Test
	void testRefreshToken_RefreshTokenExpirado() {

		// Arrange
		refreshTokenEntity.setExpiracion(Instant.now().minusMillis(3600000));

		Mockito.when(refreshTokenRepo.findByToken("expired-token")).thenReturn(Optional.of(refreshTokenEntity));

		refreshTokenRequest.setToken("expired-token");

		// Act & Assert
		AurkituException exception = assertThrows(AurkituException.class,
				() -> authService.refreshToken(refreshTokenRequest));

		assertEquals(ErrorEnum.REFRESH_TOKEN_CADUCADO.getStatus(), exception.getStatusCode());
		Mockito.verify(refreshTokenRepo).delete(refreshTokenEntity);
		Mockito.verify(refreshTokenRepo, never()).save(any());
		Mockito.verify(jwtUtils, never()).generateAccessToken(any());
	}
}