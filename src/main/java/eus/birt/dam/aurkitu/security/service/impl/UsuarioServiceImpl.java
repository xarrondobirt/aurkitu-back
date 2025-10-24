package eus.birt.dam.aurkitu.security.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import eus.birt.dam.aurkitu.dto.UsuarioDTO;
import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import eus.birt.dam.aurkitu.mail.service.MailService;
import eus.birt.dam.aurkitu.model.CodigoVerificacionEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;
import eus.birt.dam.aurkitu.payload.request.RegistroUsuarioRequest;
import eus.birt.dam.aurkitu.payload.response.MensajeResponse;
import eus.birt.dam.aurkitu.payload.response.RegistroUsuarioResponse;
import eus.birt.dam.aurkitu.security.persistence.CodigoVerificacionRepository;
import eus.birt.dam.aurkitu.security.persistence.UsuarioRepository;
import eus.birt.dam.aurkitu.security.service.UsuarioService;
import eus.birt.dam.aurkitu.utils.Constantes;
import eus.birt.dam.aurkitu.utils.GeneradorCodigos;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio que implementa la interfaz {@link UsuarioService}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioRepository usuarioRepo;
	private final CodigoVerificacionRepository codVerificacionRepo;
	private final MailService mailService;

	@Override
	@Transactional
	public RegistroUsuarioResponse registrarUsuario(UsuarioDTO usuarioDTO) {
		log.info("AUTH - SERVICE - REGISTRO");

		// Verificar si el email ya existe
		if (usuarioRepo.existsByEmail(usuarioDTO.getEmail())) {
			throw new AurkituException(ErrorEnum.EMAIL_ALREADY_EXISTS);
		}

		// Verificar si el username está disponible
		if (usuarioRepo.existsByUsername(usuarioDTO.getUsername())) {
			throw new AurkituException(ErrorEnum.USERNAME_ALREADY_EXISTS);
		}

		// Encriptar password con MD5
		String passwordMd5 = DigestUtils.md5DigestAsHex(usuarioDTO.getPassword().getBytes());

		// Crear y guardar usuario
		UsuarioEntity usuario = UsuarioEntity.builder().username(usuarioDTO.getUsername()).email(usuarioDTO.getEmail())
				.password(passwordMd5).verificado(false).build();

		UsuarioEntity usuarioNuevo = usuarioRepo.save(usuario);

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

		return new MensajeResponse(Constantes.EMAIL_VERIFICADO);
	}
}
