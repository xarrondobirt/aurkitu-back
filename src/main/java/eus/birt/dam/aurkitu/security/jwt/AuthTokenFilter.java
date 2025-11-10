package eus.birt.dam.aurkitu.security.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.exception.APIError;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Filtro de autenticación que procesa tokens JWT en las solicitudes HTTP.
 * Se ejecuta una vez por cada petición antes de llegar al controlador
 */
@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

	private final JwtUtils jwtUtils;
	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String token = jwtUtils.extractToken(request);

		// Excluir endpoints de auth del filtro JWT
		String path = request.getServletPath();
		if (path.equals("/api/auth/refresh-token")) {
			chain.doFilter(request, response);
			return;
		}

		try {
			if (token != null && jwtUtils.validateJwtToken(token)) {

				Integer userId = jwtUtils.getUserIdFromToken(token);

				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null,
						List.of());
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (Exception e) {

			/*
			 * Cuando se está verificando el token aún no está activo el handler global, por eso hay que gestionarlo
			 * aquí como caso especial. Casi siempre cuando el token no es válido y/o está expirado.
			 */
			APIError error = new APIError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
					ErrorEnum.ACCES_TOKEN_CADUCADO.getMessage(), request);
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write(objectMapper.writeValueAsString(error));
			return;
		}

		chain.doFilter(request, response);
	}
}