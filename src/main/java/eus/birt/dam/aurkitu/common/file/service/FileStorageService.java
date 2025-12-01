package eus.birt.dam.aurkitu.common.file.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interfaz que define los métodos para el envío de mails
 */
public interface FileStorageService {

	/**
	 * Hace la subida de una foto al servidor
	 * 
	 * @param archivo Foto que se va a subir al servidor
	 * @return url de la foto
	 */
	String guardarFoto(MultipartFile archivo);

	/**
	 * Hace la subida de un documento al servidor
	 * 
	 * @param archivo Documento que se va a subir al servidor
	 * @return url del documento
	 */
	String guardarDocumento(MultipartFile archivo);

}
