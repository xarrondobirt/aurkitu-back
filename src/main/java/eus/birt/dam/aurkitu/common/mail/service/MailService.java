package eus.birt.dam.aurkitu.common.mail.service;

/**
 * Interfaz que define los métodos para el envío de mails
 */
public interface MailService {

	/**
	 * Envía un mail con un código de verificación
	 * 
	 * @param email    Direeción de email
	 * @param codigo   Código de verificación generado
	 * @param template HTML que se mostrará en el
	 * @param asunto   Asunto del email
	 */
	void enviarCodigo(String email, String codigo, String template, String asunto);

}
