package eus.birt.dam.aurkitu.mail.service;

/**
 * Interfaz que define los métodos para el envío de mails
 */
public interface MailService {

	/**
	 * Envía un mail con el código de verificación para completar el registro
	 * 
	 * @param email  Direeción de email
	 * @param codigo Código de verificación generado
	 */
	void enviarCodigoVerificacion(String email, String codigo);

}
