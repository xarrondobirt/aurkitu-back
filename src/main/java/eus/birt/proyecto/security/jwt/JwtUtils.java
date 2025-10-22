package eus.birt.proyecto.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import eus.birt.proyecto.enums.ErrorEnum;
import eus.birt.proyecto.exception.CustomResponseStatusException;
import eus.birt.proyecto.model.UsuarioEntity;
import eus.birt.proyecto.utils.Constantes;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

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
	 * Método para generar token JWT
	 * 
	 * @param usuario Usuario al que se asignará el token
	 * @return Token JWT
	 */
	public String generateAccessToken(UsuarioEntity usuario) {
		return Jwts.builder().subject(usuario.getEmail()).claim("userId", usuario.getId())
				.claim("username", usuario.getUsername()).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + jwtAccessExpiration))
				.signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).compact();

	}

	public String generateRefreshToken(UsuarioEntity usuario) {
		return Jwts.builder().subject(usuario.getEmail()).claim("type", "refresh").issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + jwtRefreshExpiration))
				.signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).compact();
	}

	public boolean validateJwtToken(String authToken) {
		boolean bToken = false;
		try {
			Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build().parseSignedClaims(authToken);
		} catch (JwtException e) {
			log.error(ErrorEnum.SESION_ERROR.getMessage(), e.getMessage());
			throw new CustomResponseStatusException(ErrorEnum.SESION_ERROR);
		}

		return bToken;
	}

	public String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(Constantes.AUTH);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constantes.BEARER)) {
			return bearerToken.substring(7); // Extrae el token sin "Bearer "
		}
		return null;
	}

	public Integer getUserIdFromToken(String token) {
		return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build().parseSignedClaims(token)
				.getPayload().get("userId", Integer.class); // Extrae el claim "userId"
	}
}