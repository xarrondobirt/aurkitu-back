package eus.birt.dam.aurkitu.common.file.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import eus.birt.dam.aurkitu.common.file.service.FileStorageService;
import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación de la interfaz {@link FileStorageService}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

	@Value("${app.uploads.foto.directory}")
	private String fotoDir;

	@Value("${app.uploads.doc.directory}")
	private String docDir;

	@Override
	public String guardarFoto(MultipartFile archivo) {
		return this.guardarArchivo(archivo, fotoDir);
	}

	@Override
	public String guardarDocumento(MultipartFile archivo) {
		return this.guardarArchivo(archivo, docDir);
	}

	/**
	 * Guarda un archivo en el sistema y genera una URL de acceso
	 * 
	 * @param archivo    Archivo a guardar
	 * @param directorio Directorio de destino donde se guardará el archivo
	 * @return URL relativa para acceder al archivo guardado
	 * @throws AurkituException Si ocurre un error al guardar el archivo
	 */
	private String guardarArchivo(MultipartFile archivo, String directorio) {

		try {
			String nombreArchivo = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
			Path ruta = Paths.get(directorio, nombreArchivo);

			// Crear directorio si no existe
			Files.createDirectories(ruta.getParent());

			// Guardar archivo
			Files.copy(archivo.getInputStream(), ruta);

			log.info("Archivo guardado: {}", ruta.toString());
			return nombreArchivo;

		} catch (IOException e) {
			throw new AurkituException(ErrorEnum.SUBIR_ARCHIVO);
		}
	}
}
