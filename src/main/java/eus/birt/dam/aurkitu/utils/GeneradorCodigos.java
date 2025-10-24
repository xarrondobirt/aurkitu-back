package eus.birt.dam.aurkitu.utils;

import java.security.SecureRandom;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utilidad para generar códigos
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneradorCodigos {
	private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final SecureRandom RANDOM = new SecureRandom();

	/**
	 * Genera un código alfanúmerico del tamaño indicado
	 * 
	 * @param size Tamaño del código
	 * @return Código alfanúmerico con el tamaño indicado
	 */
	public static String generarCodigo(int size) {
		StringBuilder codigo = new StringBuilder(size);
		for (int i = 0; i < size; i++) {
			codigo.append(CARACTERES.charAt(RANDOM.nextInt(CARACTERES.length())));
		}
		return codigo.toString();
	}
}