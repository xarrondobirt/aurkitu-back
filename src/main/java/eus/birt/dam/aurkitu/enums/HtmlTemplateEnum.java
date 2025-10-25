package eus.birt.dam.aurkitu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum para las plantilas html
 */
@Getter
@RequiredArgsConstructor
public enum HtmlTemplateEnum {

	VERIFICAR_EMAIL, RECUPERAR_PASSWORD;

	@Override
	public String toString() {
		return name().toLowerCase().replace("_", "-");
	}

}
