package eus.birt.dam.aurkitu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum para estados de los objetos
 */
@Getter
@RequiredArgsConstructor
public enum EstadoObjetoEnum {
	PERDIDO, ENCONTRADO, DEVUELTO;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
