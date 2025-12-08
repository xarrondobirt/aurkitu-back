package eus.birt.dam.aurkitu.common.mail.service.impl;

import java.nio.charset.StandardCharsets;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import eus.birt.dam.aurkitu.common.mail.service.MailService;
import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
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
	public void enviarCodigo(String email, String codigo, String template, String asunto) {

//		log.info("MAIL - SERVICE - ENVIAR CÓDIGO");

		try {
			Context context = new Context();
			context.setVariable("appName", Constantes.APP_NAME);
			context.setVariable("email", email);
			context.setVariable("codigo", codigo);

			String htmlContent = templateEngine.process(template, context);

			MimeMessage mensaje = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, StandardCharsets.UTF_8.displayName());
			helper.setTo(email);
			helper.setSubject(asunto + Constantes.APP_NAME);
			helper.setText(htmlContent, true);

			mailSender.send(mensaje);
			log.info("Email enviado a {}", email);

		} catch (MessagingException e) {
			log.error("Error enviando email a {}: {}", email, e.getMessage());
			throw new AurkituException(ErrorEnum.EMAIL_SEND_ERROR);
		}
	}
}
