package eus.birt.dam.aurkitu.config;

import eus.birt.dam.aurkitu.security.jwt.AuthTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import eus.birt.dam.aurkitu.enums.ErrorEnum;
import eus.birt.dam.aurkitu.exception.AurkituException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {

    //Inyetar el filtro JWT
    private final AuthTokenFilter jwtAuthenticationFilter;

    public WebSecurityConfig(AuthTokenFilter authTokenFilter) {
        this.jwtAuthenticationFilter = authTokenFilter;
    }

	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
		//http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        //Se activa CORS
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        //Se deshabilita CSRF
        .csrf(AbstractHttpConfigurer::disable)
        //No se crean sesiones con cookies
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //Se permite el acceso sin autenticación a ciertos endpoints
        .authorizeHttpRequests(auth -> auth
                // Rutas públicas (Login, Registro, Verificar email, Uploads, Error)
                .requestMatchers("/v1/auth/**", "/error").permitAll()
                //Permitimos acceso a las imágenes subidas solo por GET
                .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                // El resto requiere autenticación (JWT)
                .anyRequest().authenticated()
        )
        //Ejecutar el filtro JWT antes de comprobar si el usuario está logeado y meerlo en el contexto de seguridad
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
	}

    /**
     * BEAN DE CONFIGURACIÓN CORS CENTRALIZADA
     * Aquí definimos quién tiene permiso para hablar con el Backend.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // LISTA DE ORÍGENES PERMITIDOS
        configuration.setAllowedOrigins(Arrays.asList(
                // PRODUCCIÓN (El dominio real desde donde carga Angular)
                "https://673bd45f-8f51-4f9e-bfdf.ad161612c2a6.pc.birt.eus",
                "https://23e1799f-64b7-4fd3-ba1e-c6bd129c353a.pc.birt.eus",
                // DESARROLLO (Túnel SSH, vital para tu entorno actual)
                "https://localhost:8443",
                // LOCALHOST (Desarrollo Angular estándar)
                "http://localhost:4200",
                // IONIC (Desarrollo móvil local)
                "http://localhost:8100",
                // CAPACITOR (Móviles nativos)
                "http://localhost",
                "https://localhost", //Es el que usa el emulador en Android Studio (Pixel con Android 13)
                "capacitor://localhost"
        ));

        // Verbos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        // Cabeceras que Angular puede enviar (Authorization es clave para JWT)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Origin", "Accept", "X-Requested-With"));
        // Permitir envío de credenciales/cookies
        configuration.setAllowCredentials(true);
        // Caché de la respuesta (1 hora)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * UserDetailsService vacío para evitar warning de Spring. En la práctica la
	 * aplicación funciona
	 * mediante JWT así se evita que Spring genere una contraseña por defecto.
	 * 
	 * @return
	 */
	@Bean
	UserDetailsService userDetailsService() {
		return username -> {
			log.warn("Intento de autenticación básica con usuario: {}", username);
			throw new AurkituException(ErrorEnum.JWT_AUTH_REQUERIDA);
		};
	}
}
