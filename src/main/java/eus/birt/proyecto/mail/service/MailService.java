package eus.birt.proyecto.mail.service;

import org.springframework.stereotype.Service;

/**
 * Interfaz que define los métodos para el envío de mails
 */
@Service
public interface MailService {

	void enviarCodigoVerificacion(String email, String codigo);

}
