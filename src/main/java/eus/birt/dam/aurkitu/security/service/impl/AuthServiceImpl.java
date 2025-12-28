package eus.birt.dam.aurkitu.security.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eus.birt.dam.aurkitu.common.mail.service.MailService;
import eus.birt.dam.aurkitu.dto.UsuarioDTO;
import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.enums.HtmlTemplateEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import eus.birt.dam.aurkitu.model.CodigoVerificacionEntity;
import eus.birt.dam.aurkitu.model.RefreshTokenEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;
import eus.birt.dam.aurkitu.payload.request.LoginRequest;
import eus.birt.dam.aurkitu.payload.request.RefreshTokenRequest;
import eus.birt.dam.aurkitu.payload.request.RegistroUsuarioRequest;
import eus.birt.dam.aurkitu.payload.request.ResetPasswordRequest;
import eus.birt.dam.aurkitu.payload.response.LoginResponse;
import eus.birt.dam.aurkitu.payload.response.MensajeInfoResponse;
import eus.birt.dam.aurkitu.payload.response.RegistroUsuarioResponse;
import eus.birt.dam.aurkitu.security.jwt.JwtUtils;
import eus.birt.dam.aurkitu.security.persistence.CodigoVerificacionRepository;
import eus.birt.dam.aurkitu.security.persistence.RefreshTokenRepository;
import eus.birt.dam.aurkitu.security.persistence.UsuarioRepository;
import eus.birt.dam.aurkitu.security.service.AuthService;
import eus.birt.dam.aurkitu.utils.Constantes;
import eus.birt.dam.aurkitu.utils.GeneradorCodigos;
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

	private final UsuarioRepository usuarioRepo;
	private final CodigoVerificacionRepository codVerificacionRepo;
	private final MailService mailService;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenRepository refreshTokenRepo;
	private final JwtUtils jwtUtils;

	@Value("${eus.birt.proyecto.jwtRefreshExpirationMs}")
	private int refreshTokenDurationMs;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public RegistroUsuarioResponse registrarUsuario(UsuarioDTO usuarioDTO) {

		Optional<UsuarioEntity> usuarioExistente = usuarioRepo.findByEmail(usuarioDTO.getEmail());
		if (usuarioExistente.isPresent()) {
			UsuarioEntity usuario = usuarioExistente.get();

			// Si el usuario existe pero NO está verificado, reenviar código
			if (!usuario.isVerificado()) {

				// Eliminar códigos anteriores
				codVerificacionRepo.deleteByUsuario(usuario);

				// Generar y guardar nuevo código
				String codigo = GeneradorCodigos.generarCodigo(6);
				codVerificacionRepo.save(CodigoVerificacionEntity.builder().codigo(codigo).usuario(usuario).build());

				// Reenviar email
				mailService.enviarCodigo(usuario.getEmail(), codigo, HtmlTemplateEnum.VERIFICAR_EMAIL.toString(),
						Constantes.ASUNTO_VERIFICACION);

				return new RegistroUsuarioResponse(usuario.getId(), Constantes.USUARIO_SIN_VERIFICAR);
			} else {

				// Si ya está verificado, lanzar error
				throw new AurkituException(ErrorEnum.EMAIL_ALREADY_EXISTS);
			}
		}

		// Usuario sin registrar
		if (usuarioRepo.existsByUsername(usuarioDTO.getUsername())) {
			throw new AurkituException(ErrorEnum.USERNAME_ALREADY_EXISTS);
		}

		// Encriptar password
		String passwordBcrypt = passwordEncoder.encode(usuarioDTO.getPassword());

		// Crear y guardar usuario
		UsuarioEntity usuario = UsuarioEntity.builder().username(usuarioDTO.getUsername()).email(usuarioDTO.getEmail())
				.password(passwordBcrypt).verificado(false).build();

		UsuarioEntity usuarioNuevo = usuarioRepo.save(usuario);

		String codigo = GeneradorCodigos.generarCodigo(6);

		codVerificacionRepo.save(CodigoVerificacionEntity.builder().codigo(codigo).usuario(usuarioNuevo).build());

		// Envío del código vía mail
		mailService.enviarCodigo(usuario.getEmail(), codigo, HtmlTemplateEnum.VERIFICAR_EMAIL.toString(),
				Constantes.ASUNTO_VERIFICACION);

		return new RegistroUsuarioResponse(usuarioNuevo.getId(), Constantes.USUARIO_SIN_VERIFICAR);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public MensajeInfoResponse verificarCodigo(RegistroUsuarioRequest request) {

		UsuarioEntity usuario = usuarioRepo.findById(request.getIdUsuario())
				.orElseThrow(() -> new AurkituException(ErrorEnum.USER_NOT_FOUND));

		// Verificar si ya está verificado
		if (usuario.isVerificado()) {
			throw new AurkituException(ErrorEnum.USERNAME_ALREADY_VERIFIED);
		}

		// Buscar código de verificación
		CodigoVerificacionEntity codigo = codVerificacionRepo
				.findByUsuarioIdAndCodigo(request.getIdUsuario(), request.getCodigoVerificacion())
				.orElseThrow(() -> new AurkituException(ErrorEnum.VERIFICATION_CODE_NOT_FOUND));

		// Verificar expiración
		if (codigo.getExpirationDate().isBefore(Instant.now())) {
			throw new AurkituException(ErrorEnum.VERIFICATION_CODE_EXPIRED);
		}

		// Marcar usuario como verificado
		usuario.setVerificado(true);
		usuarioRepo.save(usuario);

		// Borrar el código de verificación después de usarlo
		codVerificacionRepo.delete(codigo);

		return new MensajeInfoResponse(Constantes.EMAIL_VERIFICADO);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public LoginResponse login(LoginRequest request) {

		// Validar credenciales
		UsuarioEntity usuario = usuarioRepo.findByUsername(request.getUsername())
				.orElseThrow(() -> new AurkituException(ErrorEnum.BAD_CREDENTIALS));

		// Verificar la contraseña y si ya está verificado
		if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword()) || !usuario.isVerificado()) {
			throw new AurkituException(ErrorEnum.BAD_CREDENTIALS);
		}

		// Eliminar refresh tokens anteriores del usuario
		refreshTokenRepo.deleteByUsuario(usuario);

		// Generar access token
		String accessToken = jwtUtils.generateAccessToken(usuario);

		// Manejar refresh token
		RefreshTokenEntity refreshToken = new RefreshTokenEntity();

		refreshToken.setUsuario(usuario);
		refreshToken.setExpiracion(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());

		refreshTokenRepo.save(refreshToken);

		return new LoginResponse(accessToken, refreshToken.getToken());
	}

	@Override
	@Transactional
	public void logout(String authHeader) {

//		log.info("AUTH - SERVICE - LOGOUT");

		if (authHeader == null || !authHeader.startsWith(Constantes.BEARER)) {
			throw new AurkituException(ErrorEnum.SESION_ERROR);
		}

		String accessToken = authHeader.substring(7);
		Integer idUser = jwtUtils.getUserIdFromToken(accessToken);

		UsuarioEntity usuario = usuarioRepo.findById(idUser)
				.orElseThrow(() -> new AurkituException(ErrorEnum.USER_NOT_FOUND));

		Optional<RefreshTokenEntity> refreshToken = refreshTokenRepo.findByUsuarioId(usuario.getId());

		if (refreshToken.isPresent()) {
			refreshTokenRepo.delete(refreshToken.get());
		}
	}

	@Override
	@Transactional
	public LoginResponse refreshToken(RefreshTokenRequest refreshTokenReq) {

//		log.info("AUTH - SERVICE - REFRESH TOKEN");

		// Buscar usuario y su refresh token en BD
		RefreshTokenEntity refreshToken = refreshTokenRepo.findByToken(refreshTokenReq.getToken())
				.orElseThrow(() -> new AurkituException(ErrorEnum.REFRESH_TOKEN_INVALIDO));

		// Verificar y eliminar el viejo
		if (refreshToken.isExpirado()) {
			refreshTokenRepo.delete(refreshToken);
			throw new AurkituException(ErrorEnum.REFRESH_TOKEN_CADUCADO);
		}

		refreshTokenRepo.delete(refreshToken);

		UsuarioEntity usuario = refreshToken.getUsuario();

		RefreshTokenEntity newRefreshToken = new RefreshTokenEntity();
		newRefreshToken.setUsuario(usuario);
		newRefreshToken.setToken(UUID.randomUUID().toString());
		newRefreshToken.setExpiracion(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshTokenRepo.save(newRefreshToken);

		String newAccessToken = jwtUtils.generateAccessToken(usuario);
		return (new LoginResponse(newAccessToken, newRefreshToken.getToken()));
	}

	@Override
	@Transactional
	public MensajeInfoResponse recuperarPassword(String email) {

//		log.info("AUTH - SERVICE - RECUPERAR PASSWORD");

		// Buscar usuario por email
		UsuarioEntity usuario = usuarioRepo.findByEmail(email)
				.orElseThrow(() -> new AurkituException(ErrorEnum.USER_NOT_FOUND));

		// Verificar que el usuario está verificado
		if (!usuario.isVerificado()) {
			throw new AurkituException(ErrorEnum.BAD_CREDENTIALS);
		}

		// Generar código de verificación
		String codigo = GeneradorCodigos.generarCodigo(6);

		// Guardar código en BD (reutilizando la misma tabla)
		CodigoVerificacionEntity codigoVerificacion = new CodigoVerificacionEntity();
		codigoVerificacion.setUsuario(usuario);
		codigoVerificacion.setCodigo(codigo);
		codVerificacionRepo.save(codigoVerificacion);

		// Enviar email con el código
		mailService.enviarCodigo(email, codigo, HtmlTemplateEnum.RECUPERAR_PASSWORD.toString(),
				Constantes.ASUNTO_RESET_PASSWORD);

		return new MensajeInfoResponse(Constantes.EMAIL_RECUPERAR_PASSWORD);
	}

	@Override
	@Transactional
	public MensajeInfoResponse resetPassword(ResetPasswordRequest request) {

//		log.info("AUTH - SERVICE - RESET PASSWORD");

		// Validar que las contraseñas coincidan
		if (!request.getNuevaPassword().equals(request.getRepitePassword())) {
			throw new AurkituException(ErrorEnum.PASSWORD_NO_COINCIDEN);
		}

		// Buscar usuario
//		UsuarioEntity usuario = usuarioRepo.findById(request.getIdUsuario())
//				.orElseThrow(() -> new AurkituException(ErrorEnum.USER_NOT_FOUND));
		UsuarioEntity usuario = usuarioRepo.findByEmail(request.getEmail())
				.orElseThrow(() -> new AurkituException(ErrorEnum.USER_NOT_FOUND));

		// Buscar y validar código de verificación
//		CodigoVerificacionEntity codigo = codVerificacionRepo
//				.findByUsuarioIdAndCodigo(request.getIdUsuario(), request.getCodVerificacion())
//				.orElseThrow(() -> new AurkituException(ErrorEnum.VERIFICATION_CODE_NOT_FOUND));
		CodigoVerificacionEntity codigo = codVerificacionRepo
				.findByUsuarioIdAndCodigo(usuario.getId(), request.getCodVerificacion())
				.orElseThrow(() -> new AurkituException(ErrorEnum.VERIFICATION_CODE_NOT_FOUND));

		// Verificar expiración
		if (codigo.getExpirationDate().isBefore(Instant.now())) {
			throw new AurkituException(ErrorEnum.VERIFICATION_CODE_EXPIRED);
		}

		// Actualizar contraseña
		usuario.setPassword(passwordEncoder.encode(request.getNuevaPassword()));
		usuarioRepo.save(usuario);

		// Eliminar código usado
		codVerificacionRepo.delete(codigo);

		return new MensajeInfoResponse(Constantes.PASSWORD_ACTUALIZADA);
	}
}
