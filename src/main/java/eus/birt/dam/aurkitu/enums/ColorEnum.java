package eus.birt.dam.aurkitu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum para los colores de los objetos
 */
@Getter
@RequiredArgsConstructor
public enum ColorEnum {
	DESCONOCIDO, AMARILLO, NEGRO, BLANCO, ROJO, AZUL, VERDE, NARANJA, GRIS, MARRON, OTROS;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
