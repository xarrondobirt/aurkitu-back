package eus.birt.dam.aurkitu.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que mapea la tabla usuario
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshTokenEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private UsuarioEntity usuario;

	@NotBlank
	@Column(name = "hashed_token")
	private String token;

	@NotNull
	@Column(name = "create_date")
	private final Instant createDate = Instant.now();

	@NotNull
	@Column(name = "expiration_date")
	private Instant expiracion;

	/**
	 * Método que comprueba si un refreshtoken está caducado
	 * 
	 * @return true si ha caducado, false en caso contrario
	 */
	public boolean isExpirado() {
		return this.expiracion.isBefore(Instant.now());
	}
}
