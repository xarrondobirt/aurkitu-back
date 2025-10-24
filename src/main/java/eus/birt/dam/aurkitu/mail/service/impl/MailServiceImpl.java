package eus.birt.dam.aurkitu.mail.service.impl;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import eus.birt.dam.aurkitu.mail.service.MailService;
import eus.birt.dam.aurkitu.utils.Constantes;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación de la interfaz {@link MailService}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;

	@Override
	public void enviarCodigoVerificacion(String email, String codigo) {
		log.info("MAIL - SERVICE - ENVIAR CODIGO VERIFICACION");

		try {
			Context context = new Context();
			context.setVariable("appName", Constantes.APP_NAME);
			context.setVariable("email", email);
			context.setVariable("codigo", codigo);

			String htmlContent = templateEngine.process("emailVerification", context);

			MimeMessage mensaje = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
			helper.setTo(email);
			helper.setSubject("Verifica tu cuenta - " + Constantes.APP_NAME);
			helper.setText(htmlContent, true);

			mailSender.send(mensaje);

		} catch (MessagingException e) {
			log.error("Error enviando email a {}: {}", email, e.getMessage());
			throw new AurkituException(ErrorEnum.EMAIL_SEND_ERROR);
		}
	}
}
