package eus.birt.proyecto.security.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eus.birt.proyecto.dto.UsuarioDTO;
import eus.birt.proyecto.enums.ErrorEnum;
import eus.birt.proyecto.exception.CustomResponseStatusException;
import eus.birt.proyecto.mail.service.MailService;
import eus.birt.proyecto.model.CodigoVerificacionEntity;
import eus.birt.proyecto.model.RefreshTokenEntity;
import eus.birt.proyecto.model.UsuarioEntity;
import eus.birt.proyecto.payload.request.LoginRequest;
import eus.birt.proyecto.payload.request.RegistroUsuarioRequest;
import eus.birt.proyecto.payload.response.LoginResponse;
import eus.birt.proyecto.payload.response.MensajeResponse;
import eus.birt.proyecto.payload.response.RegistroUsuarioResponse;
import eus.birt.proyecto.security.jwt.JwtUtils;
import eus.birt.proyecto.security.persistence.CodigoVerificacionRepository;
import eus.birt.proyecto.security.persistence.RefreshTokenRepository;
import eus.birt.proyecto.security.persistence.UserRepository;
import eus.birt.proyecto.security.service.AuthService;
import eus.birt.proyecto.utils.Constantes;
import eus.birt.proyecto.utils.GeneradorCodigos;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio que implementa la interfaz {@link AuthService}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepo;
	private final CodigoVerificacionRepository codVerificacionRepo;
	private final MailService mailService;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenRepository refreshTokenRepo;
	private final JwtUtils jwtUtils;

	@Value("${eus.birt.proyecto.jwtRefreshExpirationMs}")
	private int refreshTokenDurationMs;

	@Override
	@Transactional
	public RegistroUsuarioResponse registrarUsuario(UsuarioDTO usuarioDTO) {
		log.info("AUTH - SERVICE - REGISTRO");

		// Verificar si el email ya existe
		if (userRepo.existsByEmail(usuarioDTO.getEmail())) {
			throw new CustomResponseStatusException(ErrorEnum.EMAIL_ALREADY_EXISTS);
		}

		// Verificar si el username está disponible
		if (userRepo.existsByUsername(usuarioDTO.getUsername())) {
			throw new CustomResponseStatusException(ErrorEnum.USERNAME_ALREADY_EXISTS);
		}

		// Encriptar password
		String passwordBcrypt = passwordEncoder.encode(usuarioDTO.getPassword());

		// Crear y guardar usuario
		UsuarioEntity usuario = UsuarioEntity.builder().username(usuarioDTO.getUsername()).email(usuarioDTO.getEmail())
				.password(passwordBcrypt).verificado(false).build();

		UsuarioEntity usuarioNuevo = userRepo.save(usuario);

		String codigo = GeneradorCodigos.generarCodigo(6);

		codVerificacionRepo.save(CodigoVerificacionEntity.builder().codigo(codigo).usuario(usuarioNuevo).build());

		// Envío del código vía mail
		mailService.enviarCodigoVerificacion(usuario.getEmail(), codigo);

		return new RegistroUsuarioResponse(usuarioNuevo.getId(), Constantes.USUARIO_SIN_VERIFICAR);
	}

	@Override
	@Transactional
	public MensajeResponse verificarCodigo(RegistroUsuarioRequest request) {
		log.info("AUTH - SERVICE - VERIFICAR CODIGO");

		UsuarioEntity usuario = userRepo.findById(request.getIdUsuario())
				.orElseThrow(() -> new CustomResponseStatusException(ErrorEnum.USER_NOT_FOUND));

		// Verificar si ya está verificado
		if (usuario.isVerificado()) {
			throw new CustomResponseStatusException(ErrorEnum.USERNAME_ALREADY_VERIFIED);
		}

		// Buscar código de verificación
		CodigoVerificacionEntity codigo = codVerificacionRepo
				.findByUsuarioIdAndCodigo(request.getIdUsuario(), request.getCodigoVerificacion())
				.orElseThrow(() -> new CustomResponseStatusException(ErrorEnum.VERIFICATION_CODE_NOT_FOUND));

		// Verificar expiración
		if (codigo.getExpirationDate().isBefore(Instant.now())) {
			throw new CustomResponseStatusException(ErrorEnum.VERIFICATION_CODE_EXPIRED);
		}

		// Marcar usuario como verificado
		usuario.setVerificado(true);
		userRepo.save(usuario);

		return new MensajeResponse(Constantes.EMAIL_VERIFICADO);
	}

	@Override
	@Transactional
	public LoginResponse login(LoginRequest request) {
		log.info("AUTH - SERVICE - LOGIN");

		// Validar credenciales
		UsuarioEntity usuario = userRepo.findByUsername(request.getUsername())
				.orElseThrow(() -> new CustomResponseStatusException(ErrorEnum.BAD_CREDENTIALS));

		// Verificar la contraseña y si ya está verificado
		if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword()) || !usuario.isVerificado()) {
			throw new CustomResponseStatusException(ErrorEnum.BAD_CREDENTIALS);
		}

		// Generar access token
		String accessToken = jwtUtils.generateAccessToken(usuario);

		// Manejar refresh token
		RefreshTokenEntity refreshToken = new RefreshTokenEntity();

		refreshToken.setUsuario(usuario);
		refreshToken.setExpiracion(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());

		refreshTokenRepo.save(refreshToken);

		return new LoginResponse(accessToken);
	}

	@Override
	@Transactional
	public void logout(String authHeader) {
		log.info("AUTH - SERVICE - LOGOUT");

		if (authHeader == null || !authHeader.startsWith(Constantes.BEARER)) {
			throw new CustomResponseStatusException(ErrorEnum.SESION_ERROR);
		}

		String accessToken = authHeader.substring(7);
		Integer idUser = jwtUtils.getUserIdFromToken(accessToken);

		UsuarioEntity usuario = userRepo.findById(idUser)
				.orElseThrow(() -> new CustomResponseStatusException(ErrorEnum.USER_NOT_FOUND));

		Optional<RefreshTokenEntity> refreshToken = refreshTokenRepo.findByUsuarioId(usuario.getId());

		if (refreshToken.isPresent()) {
			refreshTokenRepo.delete(refreshToken.get());
		}
	}

	@Override
	@Transactional
	public LoginResponse refreshToken(String authHeader) {
		log.info("AUTH - SERVICE - REFRESH TOKEN");

		if (authHeader == null || !authHeader.startsWith(Constantes.BEARER)) {
			throw new CustomResponseStatusException(ErrorEnum.SESION_ERROR);
		}

		String accessToken = authHeader.substring(7);
		Integer idUser = jwtUtils.getUserIdFromToken(accessToken);

		// Buscar usuario y su refresh token en BD
		RefreshTokenEntity refreshToken = refreshTokenRepo.findByUsuarioId(idUser)
				.orElseThrow(() -> new CustomResponseStatusException(ErrorEnum.REFRESH_TOKEN_INVALIDO));

		// Verificar y eliminar el viejo
		if (refreshToken.getExpiracion().isBefore(Instant.now())) {
			refreshTokenRepo.delete(refreshToken);
			throw new CustomResponseStatusException(ErrorEnum.REFRESH_TOKEN_CADUCADO);
		}

		refreshTokenRepo.delete(refreshToken);

		UsuarioEntity usuario = userRepo.findById(idUser)
				.orElseThrow(() -> new CustomResponseStatusException(ErrorEnum.USER_NOT_FOUND));

		RefreshTokenEntity newRefreshToken = new RefreshTokenEntity();
		newRefreshToken.setUsuario(usuario);
		newRefreshToken.setToken(UUID.randomUUID().toString());
		newRefreshToken.setExpiracion(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshTokenRepo.save(newRefreshToken);

		String newAccessToken = jwtUtils.generateAccessToken(usuario);
		return (new LoginResponse(newAccessToken));
	}
}
