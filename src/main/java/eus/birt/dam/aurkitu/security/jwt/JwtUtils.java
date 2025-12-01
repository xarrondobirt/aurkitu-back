package eus.birt.dam.aurkitu.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import eus.birt.dam.aurkitu.model.UsuarioEntity;
import eus.birt.dam.aurkitu.utils.Constantes;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Utilidad para la gestión de tokens JWT.
 * Maneja tanto tokens de acceso como de refresco con diferentes tiempos de
 * expiración.
 */
@Component
@Slf4j
public class JwtUtils {

	@Value("${eus.birt.proyecto.jwtSecret}")
	private String jwtSecret;

	@Value("${eus.birt.proyecto.jwtAccessExpirationMs}")
	private int jwtAccessExpiration;

	@Value("${eus.birt.proyecto.jwtRefreshExpirationMs}")
	private int jwtRefreshExpiration;

	/**
	 * Genera un token de acceso JWT para un usuario
	 * 
	 * @param usuario Entidad de usuario para la cual generar el token
	 * @return Token JWT de acceso
	 */
	public String generateAccessToken(UsuarioEntity usuario) {
		return Jwts.builder().subject(usuario.getEmail()).claim("userId", usuario.getId())
				.claim("username", usuario.getUsername()).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + jwtAccessExpiration))
				.signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).compact();

	}

	/**
	 * Genera un token de refresco JWT para un usuario
	 * 
	 * @param usuario Entidad de usuario para la cual generar el token
	 * @return Token JWT de refresco
	 */
	public String generateRefreshToken(UsuarioEntity usuario) {
		return Jwts.builder().subject(usuario.getEmail()).claim("type", "refresh").issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + jwtRefreshExpiration))
				.signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).compact();
	}

	/**
	 * Valida la integridad y expiración de un token JWT
	 * 
	 * @param authToken Token JWT a validar
	 * @return true si el token es válido, false en caso contrario
	 * @throws CustomResponseStatusException si el token es inválido o ha expirado
	 */
	public boolean validateJwtToken(String authToken) {
		boolean bToken = false;
		try {

			Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build().parseSignedClaims(authToken);
			bToken = true;
		} catch (ExpiredJwtException e) {
			log.error(ErrorEnum.ACCES_TOKEN_CADUCADO.getMessage(), e.getMessage());
			throw new AurkituException(ErrorEnum.ACCES_TOKEN_CADUCADO);

		} catch (JwtException e) {
			log.error(ErrorEnum.SESION_ERROR.getMessage(), e.getMessage());
			throw new AurkituException(ErrorEnum.SESION_ERROR);
		}

		return bToken;
	}

	/**
	 * Extrae el token JWT del header Authorization de la solicitud HTTP
	 * 
	 * @param request Solicitud HTTP de la cual extraer el token
	 * @return Token JWT sin el prefijo "Bearer", o null si no está presente
	 */
	public String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(Constantes.AUTH);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constantes.BEARER)) {
			return bearerToken.substring(7); // Extrae el token sin "Bearer "
		}
		return null;
	}

	/**
	 * Extrae el ID de usuario del token JWT
	 * 
	 * @param token Token JWT del cual extraer el ID de usuario
	 * @return ID de usuario contenido en el token
	 */
	public Integer getUserIdFromToken(String token) {
		return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build().parseSignedClaims(token)
				.getPayload().get("userId", Integer.class); // Extrae el claim "userId"
	}

	public Integer getUserIdFromRequest(HttpServletRequest request) {
		String authHeader = request.getHeader(Constantes.AUTH);

		if (authHeader == null || !authHeader.startsWith(Constantes.BEARER)) {
			throw new AurkituException(ErrorEnum.SESION_ERROR);
		}

		String accessToken = authHeader.substring(7);
		return this.getUserIdFromToken(accessToken);
	}
}