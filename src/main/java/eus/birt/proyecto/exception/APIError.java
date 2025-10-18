package eus.birt.proyecto.exception;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

/**
 * Clase que representa un error personalizado en formato JSON.
 */
@Data
public class APIError {

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ")
	private ZonedDateTime timestamp;
	private int status;
	private String error;
	private String message;
	private String path;

	/**
	 * Constructor APIError
	 *
	 * @param i                  Código de estado HTTP.
	 * @param error              Tipo de error.
	 * @param message            Mensaje descriptivo del error.
	 * @param httpServletRequest HttpServletRequest
	 */
	public APIError(int i, String error, String message, HttpServletRequest httpServletRequest) {
		super();

		this.setTimestamp(ZonedDateTime.now());
		this.status = i;
		this.error = error;
		this.message = message;
		this.path = buildPath(httpServletRequest);
	}

	/**
	 * Construye la ruta completa que generó el error
	 * 
	 * @param httpServletRequest HttpServletRequest
	 * @return La ruta completa que generó el error.
	 */
	private String buildPath(HttpServletRequest httpServletRequest) {
		path = httpServletRequest.getRequestURI();

		Map<String, String[]> mapa = Collections.list(httpServletRequest.getParameterNames()).stream()
				.collect(Collectors.toMap(parameterName -> parameterName, httpServletRequest::getParameterValues));

		Optional<String> firstKey = mapa.keySet().stream().findFirst();

		firstKey.ifPresent(s -> mapa.forEach((k, v) -> {
			String sepParam = "&";
			String queryParam = k + "=" + v[0];

			if (s.equals(k)) {
				sepParam = "?";
			}
			path = path.concat(sepParam + queryParam);
		}));

		return path;
	}
}
