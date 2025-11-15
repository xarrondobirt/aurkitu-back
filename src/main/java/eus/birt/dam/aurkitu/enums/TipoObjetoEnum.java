package eus.birt.dam.aurkitu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum para los tipo de objetos
 */
@Getter
@RequiredArgsConstructor
public enum TipoObjetoEnum {
	DESCONOCIDO, SMARTPHONE, AURICULARES, BOLSO, CARTERA, LLAVES, GAFAS, PARAGUAS, DOCUMENTACION, PRENDA, OTROS;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
