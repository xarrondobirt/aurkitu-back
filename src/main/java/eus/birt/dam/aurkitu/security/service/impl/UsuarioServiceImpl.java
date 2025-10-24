package eus.birt.proyecto.security.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import eus.birt.proyecto.dto.UsuarioDTO;
import eus.birt.proyecto.enums.ErrorEnum;
import eus.birt.proyecto.exception.CustomResponseStatusException;
import eus.birt.proyecto.mail.service.MailService;
import eus.birt.proyecto.model.CodigoVerificacionEntity;
import eus.birt.proyecto.model.UsuarioEntity;
import eus.birt.proyecto.payload.request.RegistroUsuarioRequest;
import eus.birt.proyecto.payload.response.MensajeResponse;
import eus.birt.proyecto.payload.response.RegistroUsuarioResponse;
import eus.birt.proyecto.security.persistence.CodigoVerificacionRepository;
import eus.birt.proyecto.security.persistence.UserRepository;
import eus.birt.proyecto.security.service.UserService;
import eus.birt.proyecto.utils.Constantes;
import eus.birt.proyecto.utils.GeneradorCodigos;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio que implementa la interfaz {@link UserService}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepo;
	private final CodigoVerificacionRepository codVerificacionRepo;
	private final MailService mailService;

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

		// Encriptar password con MD5
		String passwordMd5 = DigestUtils.md5DigestAsHex(usuarioDTO.getPassword().getBytes());

		// Crear y guardar usuario
		UsuarioEntity usuario = UsuarioEntity.builder().username(usuarioDTO.getUsername()).email(usuarioDTO.getEmail())
				.password(passwordMd5).verificado(false).build();

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
}
