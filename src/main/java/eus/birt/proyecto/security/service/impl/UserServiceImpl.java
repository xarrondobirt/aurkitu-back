package eus.birt.proyecto.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import eus.birt.proyecto.dto.UsuarioDTO;
import eus.birt.proyecto.exception.UnauthorizedException;
import eus.birt.proyecto.model.UsuarioEntity;
import eus.birt.proyecto.payload.response.MensajeResponse;
import eus.birt.proyecto.security.persistence.UserRepository;
import eus.birt.proyecto.security.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio que implementa la interfaz {@link UserService}
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public MensajeResponse registrarUsuario(UsuarioDTO usuarioDTO) {
		log.info("AUTH - SERVICE - REGISTRO");

		// Verificar si el email ya existe
		if (userRepo.existsByEmail(usuarioDTO.getEmail())) {
			throw new UnauthorizedException("El email ya está registrado");
		}

		// Encriptar password con MD5
		String passwordMd5 = DigestUtils.md5DigestAsHex(usuarioDTO.getPassword().getBytes());

		// Crear y guardar usuario
		UsuarioEntity usuario = UsuarioEntity.builder().username(usuarioDTO.getUsername()).email(usuarioDTO.getEmail())
				.password(passwordMd5).build();

		userRepo.save(usuario);

		return new MensajeResponse("Usuario registrado correctamente");
	}
}
