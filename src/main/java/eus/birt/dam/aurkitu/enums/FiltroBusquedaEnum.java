package eus.birt.dam.aurkitu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum para los filtros aplicables a la búsqueda de objetos
 */
@Getter
@RequiredArgsConstructor
public enum FiltroBusquedaEnum {
	ID, UBICACION, TIPO, COLOR, ESTADO, FECHA, DESCRIPCION, MARCA, SERIE;

	@Override
	public String toString() {
		return name().toLowerCase().replace("_", "");
	}
}
