package eus.birt.proyecto.mail.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import es.eroski.misiongates.enums.EstadoEnum;
import es.eroski.misiongates.enums.MailEnum;
import es.eroski.misiongates.enums.SubestadoEnum;
import es.eroski.misiongates.exception.InternalServerErrorException;
import es.eroski.misiongates.model.MailDestinoEntity;
import es.eroski.misiongates.model.MailEntity;
import es.eroski.misiongates.model.MovimientoDetalleEntity;
import es.eroski.misiongates.model.sic.CodDescrGenerico;
import es.eroski.misiongates.model.sic.MailPlanos;
import es.eroski.misiongates.utils.ConstantesError;
import es.eroski.misiongates.utils.ConstantesMail;
import eus.birt.proyecto.mail.service.MailService;
import eus.birt.proyecto.model.UsuarioEntity;
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

	@Override
	public void enviarCodigoVerificacion(String email, String codigo) {
		log.info("MAIL - SERVICE - ENVIAR CODIGO VERIFICACION");

		try {
			Long idSubestado = mov.getSubestado().getIdSubestado();
			Long idEstado = mov.getSubestado().getEstado().getId();

			MailEntity mailAnular;
			List<Long> codigosArticulo = new ArrayList<>();
			List<MailPlanos> listaMailPlanos = new ArrayList<>();

			// Movimientos sin integrar en Pre-Gredisa
			if (idEstado.equals(EstadoEnum.CONFIRMACION_DE_TERCEROS.ordinal() + 1L)
					|| (idSubestado.equals(SubestadoEnum.PENDIENTE_ENVIO.getIdSubestado())
							&& idEstado.equals(EstadoEnum.FINALIZADO_COMERCIAL.ordinal() + 1L))) {

				mailAnular = mailRepo.findByDescripcion(MailEnum.ANULACION_MOV.name())
						.orElseThrow(() -> new NotFoundException(ConstantesError.MAIL_NO_ENCONTRADO));

				// Comprobar si alguna referencia ha pasado por planos
				boolean bFlagPlano = mov.getMovimientosDetalle().stream()
						.anyMatch(detalle -> "V".equals(detalle.getFlagPlanos()));

				codigosArticulo = mov.getMovimientosDetalle().stream().map(MovimientoDetalleEntity::getCodArticulo)
						.collect(Collectors.toList());

				if (bFlagPlano) {

					listaMailPlanos = mailSicRepo.mailsPlanos(sesion.getLoginSic(), codigosArticulo);
				}

				// Movimientos integrados en Pre-Gredisa
			} else if (idEstado.equals(EstadoEnum.FINALIZADO_COMERCIAL.ordinal() + 1L)
					&& !idSubestado.equals(SubestadoEnum.PENDIENTE_ENVIO.getIdSubestado())) {
				mailAnular = mailRepo.findByDescripcion(MailEnum.ANULACION_MOV_INTEGRADA.name())
						.orElseThrow(() -> new NotFoundException(ConstantesError.MAIL_NO_ENCONTRADO));

			} else {
				return;
			}

			List<CodDescrGenerico> denominaciones = panelInicioSicRepo.obtenerDenominacion(codigosArticulo);

			// Crear el mensaje del mail
			List<String> mailsDestino = mailAnular.getMailDestino().stream()
					.filter(destino -> "S".equals(destino.getActivo())).map(MailDestinoEntity::getEmail)
					.collect(Collectors.toList());
			mailsDestino.addAll(listaMailPlanos.stream().flatMap(mailPlanos -> mailPlanos.getMails().stream())
					.collect(Collectors.toList()));

			if (!mailsDestino.isEmpty()) {

				String p1 = mov.getIdMovimiento().toString();
				String p3 = denominaciones.stream().map(codDescr -> {
					String denomInforme = codDescr.getDenomInforme();
					return codDescr.getCodigo() + " " + (denomInforme != null ? denomInforme.trim() : "");
				}).collect(Collectors.joining("; "));

				MimeMessage mimeMessage = mailSender.createMimeMessage();
				MimeMessageHelper mensaje = new MimeMessageHelper(mimeMessage, true,
						StandardCharsets.UTF_8.displayName());

				mensaje.setFrom(mailConfig.getUsername());
				String[] aMailsDestino = mailsDestino.toArray(new String[0]);
				mensaje.setTo(aMailsDestino);

				String asunto = mailAnular.getAsunto().replace("<p1>", p1);
				mensaje.setSubject(asunto);

				StringBuilder htmlText = new StringBuilder(ConstantesMail.DOCTYPE_INIT);
				htmlText.append(mailAnular.getTxt().replace("<p1>", p1).replace("<p2>", motivo).replace("<p3>", p3));

				htmlText.append(ConstantesMail.DOCTYPE_FIN);

				mensaje.setText(htmlText.toString(), true);

				mailSender.send(mimeMessage);

				UsuarioEntity user = obtDatosService.obtUsuarioSesion(sesion);

				guardarMailService.guardarMail(mailAnular, aMailsDestino, asunto, htmlText.toString(), user);

			}
		} catch (MessagingException e) {
			log.error(ConstantesError.ERROR_MAIL);
			throw new InternalServerErrorException(ConstantesError.ERROR_MAIL);
		}

	}
}
